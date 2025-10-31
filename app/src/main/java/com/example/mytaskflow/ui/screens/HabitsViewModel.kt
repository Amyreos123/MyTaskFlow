package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytaskflow.data.Habit
import com.example.mytaskflow.data.HabitRepository
import com.example.mytaskflow.data.myTaskFlowApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
// --- ИСПРАВЛЕНИЕ 1: ---
// Убираем java.time.LocalDate и добавляем java.util.Calendar
import java.util.Calendar

class HabitsViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    // Это место мы исправили на прошлом шаге, оно в порядке
    val allHabits: StateFlow<List<Habit>> = habitRepository.allHabits
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    /**
     * Добавление новой привычки в базу данных.
     */
    fun insertHabit(title: String) {
        viewModelScope.launch {
            // --- ИСПРАВЛЕНИЕ 2: ---
            // Используем 'completedDates' вместо 'completions'
            val newHabit = Habit(title = title, completedDates = emptyList())
            habitRepository.insert(newHabit)
        }
    }

    /**
     * Обработка клика по привычке (выполнение или отмена выполнения).
     */
    fun onHabitClicked(habit: Habit) {
        viewModelScope.launch {
            // --- ИСПРАВЛЕНИЕ 3: ---
            // Используем ту же логику Calendar, что и в Habit.kt,
            // чтобы получить сегодняшний таймстэмп.
            val todayTimestamp = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            // --- ИСПРАВЛЕНИЕ 4: ---
            // Используем 'completedDates' вместо 'completions'
            val updatedCompletions = habit.completedDates.toMutableList()

            // Мы используем функцию isCompletedToday() из Habit.kt
            if (habit.isCompletedToday()) {
                // Привычка уже выполнена, удаляем сегодняшний таймстэмп
                updatedCompletions.remove(todayTimestamp)
            } else {
                // Привычка не выполнена, добавляем сегодняшний таймстэмп
                updatedCompletions.add(todayTimestamp)
            }

            // --- ИСПРАВЛЕНИЕ 5: ---
            // Обновляем объект, используя 'completedDates'
            habitRepository.update(habit.copy(completedDates = updatedCompletions))
        }
    }

    // Фабрика для ViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this.myTaskFlowApplication()
                HabitsViewModel(application.habitRepository)
            }
        }
    }
}