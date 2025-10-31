package com.example.mytaskflow.data

import androidx.room.TypeConverter

/**
 * TypeConverter (Конвертер Типов) для Room.
 * "Учит" Room сохранять и извлекать типы данных,
 * которые он не поддерживает "из коробки" (например, List<Long>).
 */
class Converters {

    /**
     * Преобразует список таймстэмпов (Long) в единую строку.
     * [1678886400, 1678972800] -> "1678886400,1678972800"
     *
     * --- ИСПРАВЛЕНИЕ ---
     * Мы убираем '?' (nullable) типы. Наша модель Habit
     * и база данных НЕ допускают null для этого поля.
     */
    @TypeConverter
    fun fromTimestampList(list: List<Long>): String {
        // .joinToString() - стандартная функция Kotlin для "склеивания"
        // элементов списка в строку через разделитель.
        return list.joinToString(",")
    }

    /**
     * Преобразует строку обратно в список таймстэмпов (Long).
     * "1678886400,1678972800" -> [1678886400, 1678972800]
     *
     * --- ИСПРАВЛЕНИЕ ---
     * Мы убираем '?' (nullable) типы. Конвертер ДОЛЖЕН
     * вернуть List<Long>, а не List<Long>?, чтобы
     * соответствовать data class Habit.
     */
    @TypeConverter
    fun toTimestampList(string: String): List<Long> {
        // Если строка пустая (например, новый Habit без дат),
        // возвращаем пустой список.
        if (string.isEmpty()) {
            return emptyList()
        }
        // .split() - делит строку по разделителю ","
        // .map { it.toLong() } - превращает каждую полученную строку в Long
        return string.split(",").map { it.toLong() }
    }
}