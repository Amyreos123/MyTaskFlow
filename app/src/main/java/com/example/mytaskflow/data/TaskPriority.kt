package com.example.mytaskflow.data

/**
 * Enum для представления уровней приоритета задачи.
 * Мы храним 'value' в виде Int (2, 1, 0),
 * чтобы было легко сортировать по убыванию (DESC).
 *
 * --- НОВОЕ ---
 * Добавляем displayName для отображения в UI.
 */
enum class TaskPriority(val value: Int, val displayName: String) {
    HIGH(2, "Высокий"),
    NORMAL(1, "Обычный"),
    LOW(0, "Низкий");

    // --- НОВОЕ ---
    // Добавляем companion object, чтобы легко получать
    // Enum по его числовому значению (value) из БД.
    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: NORMAL
    }
}