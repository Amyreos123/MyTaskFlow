package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // Функция для получения всех привычек (отсутствовала)
    @Query("SELECT * FROM habits ORDER BY title ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    // Функция для обновления привычки (отсутствовала)
    @Update
    suspend fun update(habit: Habit)
}