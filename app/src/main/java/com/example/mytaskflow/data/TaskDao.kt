package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// @Dao - "Data Access Object". Говорит Room, что это интерфейс для доступа к БД.
@Dao
interface TaskDao {

    // OnConflictStrategy.IGNORE - если мы пытаемся вставить задачу с ID,
    // который уже есть, Room просто проигнорирует эту вставку.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    // suspend - функция может быть приостановлена (для асинхронной работы)
    @Update
    suspend fun update(task: Task)

    // --- НОВОЕ ---
    // Добавляем аннотацию @Delete
    @Delete
    suspend fun delete(task: Task)
    // --- КОНЕЦ НОВОГО ---

    // @Query - специальный запрос к БД.
    // "SELECT * ..." - выбрать все колонки из таблицы "tasks"
    // "ORDER BY id DESC" - сортировать по ID в обратном порядке (новые вверху)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>> // Flow - асинхронный "поток" данных
}