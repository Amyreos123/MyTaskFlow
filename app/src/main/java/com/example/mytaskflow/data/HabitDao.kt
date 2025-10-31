package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO для управления Привычками (Habit)
 */
@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    // Привычки, в отличие от Задач, не нужно сортировать
    // по дате добавления, достаточно по ID (чтобы новые были в конце).
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAllHabits(): Flow<List<Habit>>
}