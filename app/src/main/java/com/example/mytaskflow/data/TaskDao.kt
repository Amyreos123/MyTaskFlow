package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    // --- ИЗМЕНЕНИЕ ---
    // @Transaction - Гарантирует, что Room выполнит два запроса
    // (один для Task, один для SubTask) "атомарно" (как единое целое).
    // Теперь функция возвращает наш класс-контейнер TaskWithSubTasks.
    @Transaction
    @Query("SELECT * FROM tasks ORDER BY priority DESC, id DESC")
    fun getAllTasks(): Flow<List<TaskWithSubTasks>>
    // --- КОНЕЦ ИZМЕНЕНИЯ ---

    // --- НОВОЕ ---
    // Новая функция, которая понадобится нам на Шаге 7.4
    // для экрана деталей задачи.
    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskWithSubTasks(id: Int): Flow<TaskWithSubTasks>
    // --- КОНЕЦ НОВОГО ---
}