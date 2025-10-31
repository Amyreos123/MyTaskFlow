package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// @Dao говорит Room, что это Data Access Object
@Dao
interface TaskDao {

    // @Query - это наш SQL-запрос.
    // "SELECT * FROM tasks" - "Выбери все из таблицы 'tasks'"
    // Flow<...> - это "поток" данных. Наш UI автоматически обновится,
    // как только данные в таблице изменятся.
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    // @Insert - вставить новый объект (задачу)
    @Insert
    suspend fun insert(task: Task) // suspend - для Coroutines

    // @Update - обновить существующую задачу (например, отметить выполненной)
    @Update
    suspend fun update(task: Task)
}