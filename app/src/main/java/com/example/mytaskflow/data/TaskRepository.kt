package com.example.mytaskflow.data

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий - это "посредник" между ViewModel и Источником Данных (DAO).
 * Он прячет от ViewModel то, откуда берутся данные (БД, сеть и т.д.).
 */
class TaskRepository(private val taskDao: TaskDao) {

    // "Сквозной" доступ к потоку задач из DAO
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    // suspend - эти функции должны вызываться из Coroutine
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    // --- НОВОЕ ---
    // Добавляем функцию delete, которая вызывает DAO
    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }
    // --- КОНЕЦ НОВОГО ---
}