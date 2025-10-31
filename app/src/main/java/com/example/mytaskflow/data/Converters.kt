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
     */
    @TypeConverter
    fun fromTimestampList(list: List<Long>?): String? {
        // .joinToString() - стандартная функция Kotlin для "склеивания"
        // элементов списка в строку через разделитель.
        return list?.joinToString(",")
    }

    /**
     * Преобразует строку обратно в список таймстэмпов (Long).
     * "1678886400,1678972800" -> [1678886400, 1678972800]
     */
    @TypeConverter
    fun toTimestampList(string: String?): List<Long>? {
        if (string.isNullOrEmpty()) {
            return emptyList()
        }
        // .split() - делит строку по разделителю ","
        // .map { it.toLong() } - превращает каждую полученную строку в Long
        return string.split(",").map { it.toLong() }
    }
}