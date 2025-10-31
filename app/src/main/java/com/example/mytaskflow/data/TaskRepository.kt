package com.example.mytaskflow.data

import kotlinx.coroutines.flow.Flow

// Репозиторий - это обычный класс.
// Он принимает TaskDao в своем конструкторе, чтобы знать, с каким "пультом" работать.
class TaskRepository(private val taskDao: TaskDao) {

    // Эта переменная просто "пробрасывает" поток (Flow) из DAO.
    // Наш ViewModel будет 'слушать' ее.
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    // suspend-функция для вставки. Она просто вызывает такую же функцию в DAO.
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    // suspend-функция для обновления.
    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    // В будущем мы можем добавить сюда deleteTask, getTaskById и т.д.
}