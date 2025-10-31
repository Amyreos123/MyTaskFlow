package com.example.mytaskflow.data

import android.app.Application

/**
 * Класс Application - "точка входа" в приложение.
 * Мы используем его, чтобы лениво (lazy) создать
 * и хранить единственные экземпляры (singletons)
 * нашей Базы Данных и Репозиториев.
 */
class MyTaskFlowApplication : Application() {

    // Лениво создаем экземпляр Базы Данных
    private val database by lazy { AppDatabase.getDatabase(this) }

    // "Лениво" создаем Репозиторий для Задач
    val taskRepository by lazy {
        TaskRepository(database.taskDao(), database.subTaskDao())
    }

    // --- НОВОЕ ---
    // "Лениво" создаем Репозиторий для Привычек
    // (Мы создадим класс HabitRepository на следующем шаге)
    val habitRepository by lazy {
        HabitRepository(database.habitDao())
    }
    // --- КОНЕЦ НОВОГО ---
}