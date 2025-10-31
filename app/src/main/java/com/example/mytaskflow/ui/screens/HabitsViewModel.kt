package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytaskflow.data.Habit
import com.example.mytaskflow.data.HabitRepository
import com.example.mytaskflow.data.MyTaskFlowApplication // Убедись, что этот импорт есть
import com.example.mytaskflow.data.myTaskFlowApplication // И этот импорт КЛЮЧЕВОЙ
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


    // --- ИЗМЕНЕНИЕ ЗДЕСЬ ---
    // Переписываем старую фабрику на новый,
    // стандартный для нашего проекта синтаксис.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // 1. Получаем Application с помощью нашей вспомогательной функции
                val application = this.myTaskFlowApplication()

                // 2. Берем из него репозиторий
                val repository = application.habitRepository

                // 3. Создаем и возвращаем HabitsViewModel
                HabitsViewModel(repository)
            }
        }
    }
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---
}