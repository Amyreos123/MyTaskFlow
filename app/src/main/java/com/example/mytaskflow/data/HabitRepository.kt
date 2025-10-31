package com.example.mytaskflow.data

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для Привычек (Habit).
 * Является посредником между DAO и ViewModel.
 */
class HabitRepository(private val habitDao: HabitDao) {

    // "Сквозной" доступ к потоку привычек из DAO
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()

    // suspend - эти функции должны вызываться из Coroutine
    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    suspend fun update(habit: Habit) {
        habitDao.update(habit)
    }

    // (Пока нам не нужно удаление привычек,
    // но его можно будет легко добавить сюда, если понадобится)
}