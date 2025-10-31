package com.example.mytaskflow.data

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий - это "посредник" между ViewModel и Источником Данных (DAO).
 *
 * --- ИЗМЕНЕНИЕ ---
 * Теперь он управляет ДВУМЯ DAO: taskDao и subTaskDao.
 */
class TaskRepository(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao
) {

    // --- ИЗМЕНЕНИЕ ---
    // "Сквозной" доступ к потоку задач (теперь это TaskWithSubTasks)
    val allTasks: Flow<List<TaskWithSubTasks>> = taskDao.getAllTasks()
    // --- КОНЕЦ ИЗМЕНЕНИЯ ---

    // --- Функции для Task ---
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    // --- НОВОЕ: Функции для SubTask ---
    suspend fun insertSubTask(subTask: SubTask) {
        subTaskDao.insert(subTask)
    }

    suspend fun updateSubTask(subTask: SubTask) {
        subTaskDao.update(subTask)
    }

    suspend fun deleteSubTask(subTask: SubTask) {
        subTaskDao.delete(subTask)
    }

    // --- НОВОЕ: Функция для Экрана Деталей ---
    fun getTaskWithSubTasks(id: Int): Flow<TaskWithSubTasks> {
        return taskDao.getTaskWithSubTasks(id)
    }
    // --- КОНЕЦ НОВОГО ---
}