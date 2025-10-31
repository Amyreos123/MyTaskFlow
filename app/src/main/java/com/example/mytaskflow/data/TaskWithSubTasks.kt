package com.example.mytaskflow.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * POJO (Plain Old Java Object) - простой класс-контейнер.
 * Он "собирает" одну Задачу (Task) и список ее Подзадач (SubTask).
 * Room будет использовать его для автоматической "сборки" данных.
 */
data class TaskWithSubTasks(
    // @Embedded говорит Room "возьми все поля из Task и включи их сюда"
    @Embedded
    val task: Task,

    // @Relation говорит Room "а теперь найди все SubTask,
    // у которых 'taskId' (entityColumn) совпадает с 'id' (parentColumn)
    // этой Task, и положи их в этот список".
    @Relation(
        parentColumn = "id", // Поле в Task
        entityColumn = "taskId" // Поле в SubTask
    )
    val subTasks: List<SubTask>
)