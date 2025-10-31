package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mytaskflow.data.Habit
import com.example.mytaskflow.data.HabitRepository
import com.example.mytaskflow.data.MyTaskFlowApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * ViewModel для экрана "Привычки" (HabitsScreen).
 */
class HabitsViewModel(private val repository: HabitRepository) : ViewModel() {

    // 1. Поток всех привычек из репозитория.
    val allHabits: Flow<List<Habit>> = repository.allHabits

    /**
     * Добавляет новую привычку в базу данных.
     */
    fun insertHabit(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) {
            repository.insert(Habit(title = title, completedDates = emptyList()))
        }
    }

    /**
     * "Отмечает" или "снимает отметку" о выполнении привычки
     * для СЕГОДНЯШНЕГО ДНЯ.
     */
    fun onHabitClicked(habit: Habit) = viewModelScope.launch {
        // 1. Получаем таймстэмп для начала сегодняшнего дня (00:00:00)
        val todayTimestamp = getTodayStartTimestamp()

        // 2. Получаем текущий список выполненных дат
        val currentDates = habit.completedDates.toMutableList()

        // 3. Проверяем, "отмечена" ли привычка сегодня
        if (currentDates.contains(todayTimestamp)) {
            // Если да - УДАЛЯЕМ отметку
            currentDates.remove(todayTimestamp)
        } else {
            // Если нет - ДОБАВЛЯЕМ отметку
            currentDates.add(todayTimestamp)
        }

        // 4. Обновляем привычку в репозитории,
        // создав ее КОПИЮ с новым списком дат.
        repository.update(habit.copy(completedDates = currentDates))
    }

    /**
     * Вспомогательная функция для получения таймстэмпа
     * (количество миллисекунд с 1970г) для начала
     * СЕГОДНЯШНЕГО ДНЯ (00:00:00.000).
     */
    private fun getTodayStartTimestamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }


    // --- Фабрика (инструкция по сборке) ---
    // Стандартный шаблон, такой же, как у TasksViewModel,
    // но для HabitRepository.
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // 1. Получаем Application
                val application = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyTaskFlowApplication)

                // 2. Берем из него наш НОВЫЙ репозиторий
                val repository = application.habitRepository

                // 3. Создаем и возвращаем HabitsViewModel
                return HabitsViewModel(repository) as T
            }
        }
    }
}