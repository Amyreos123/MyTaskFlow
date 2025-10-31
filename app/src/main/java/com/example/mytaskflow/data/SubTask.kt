package com.example.mytaskflow.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entity (таблица) для Подзадач.
 */
@Entity(
    tableName = "sub_tasks",
    // --- НОВОЕ: Внешний ключ ---
    // Это "связывает" эту таблицу с таблицей 'tasks'
    foreignKeys = [
        ForeignKey(
            entity = Task::class, // Родительская таблица
            parentColumns = ["id"], // Ключ в родительской таблице
            childColumns = ["taskId"], // Ключ в этой таблице
            onDelete = ForeignKey.CASCADE // Если Task удаляется, все его SubTask тоже удаляются
        )
    ]
)
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Внешний ключ, ссылающийся на Task.id
    // @ColumnInfo(index = true) ускоряет запросы по этому полю
    @androidx.room.ColumnInfo(index = true)
    val taskId: Int,

    val title: String,
    val isCompleted: Boolean = false
)