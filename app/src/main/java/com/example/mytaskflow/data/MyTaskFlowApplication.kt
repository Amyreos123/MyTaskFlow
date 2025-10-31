package com.example.mytaskflow.data

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MyTaskFlowApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    val taskRepository by lazy { TaskRepository(database.taskDao(), database.subTaskDao()) }
    val habitRepository by lazy { HabitRepository(database.habitDao()) }
    val hubRepository by lazy { HubRepository(database.hubDao()) } // --- ИЗМЕНЕНИЕ ---
}

// Вспомогательная функция для получения ссылки на Application
fun CreationExtras.myTaskFlowApplication(): MyTaskFlowApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyTaskFlowApplication)