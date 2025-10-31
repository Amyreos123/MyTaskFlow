package com.example.mytaskflow.data

import android.app.Application

// Мы наследуемся от Application, чтобы этот класс
// существовал, пока живо приложение.
class MyTaskFlowApplication : Application() {

    // 'lazy' означает, что база данных будет создана только тогда,
    // когда к ней обратятся в первый раз.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    // То же самое для репозитория.
    val taskRepository: TaskRepository by lazy { TaskRepository(database.taskDao()) }
}