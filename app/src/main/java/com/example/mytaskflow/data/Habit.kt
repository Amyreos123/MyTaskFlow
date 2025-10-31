package com.example.mytaskflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar // --- ИЗМЕНЕНИЕ: Добавляем импорт ---

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
) { // --- ИЗМЕНЕНИЕ: Открываем фигурную скобку, чтобы добавить тело классу ---

    // --- ИЗМЕНЕНИЕ: Добавляем вспомогательную функцию ---
    /**
     * Вспомогательная функция, которая проверяет,
     * выполнена ли привычка СЕГОДНЯ.
     */
    fun isCompletedToday(): Boolean {
        // 1. Получаем таймстэмп для начала сегодняшнего дня (00:00:00)
        val todayTimestamp = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // 2. Проверяем, содержит ли список выполненных дат эту метку
        return completedDates.contains(todayTimestamp)
    }
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---
}