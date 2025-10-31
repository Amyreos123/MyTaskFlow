package com.example.mytaskflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hub_items")
data class HubItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val title: String, // Мы постараемся извлечь заголовок, но можем использовать URL, если не получится
    val timestamp: Long = System.currentTimeMillis() // Для сортировки по дате добавления
)