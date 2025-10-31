package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // --- ИЗМЕНЕНИЕ: Добавляем импорт ---
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // --- ИЗМЕНЕНИЕ: Эта функция была пропущена ---
    @Query("SELECT * FROM habits ORDER BY title ASC")
    fun getAllHabits(): Flow<List<Habit>>
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    // --- ИЗМЕНЕНИЕ: Эта функция была пропущена ---
    @Update
    suspend fun update(habit: Habit)
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---
}