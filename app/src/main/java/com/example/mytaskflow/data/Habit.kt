package com.example.mytaskflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity (таблица) для "Привычек".
 */
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    // Room будет использовать 'Converters' (который мы укажем в AppDatabase)
    // для преобразования этого списка в строку (String) и обратно.
    val completedDates: List<Long> = emptyList() // Храним даты как Long (таймстэмп)
)