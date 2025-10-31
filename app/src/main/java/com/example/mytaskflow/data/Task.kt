package com.example.mytaskflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity говорит Room, что этот класс - таблица в базе данных.
@Entity(tableName = "tasks")
data class Task(
    // @PrimaryKey - уникальный идентификатор.
    // autoGenerate = true - Room сам будет присваивать ID (1, 2, 3...)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val isCompleted: Boolean = false
)