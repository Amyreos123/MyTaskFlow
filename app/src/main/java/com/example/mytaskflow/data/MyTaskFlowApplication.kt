package com.example.mytaskflow.data

import android.app.Application

/**
 * Класс Application - "точка входа" в приложение.
 * Мы используем его, чтобы лениво (lazy) создать
 * и хранить единственные экземпляры (singletons)
 * нашей Базы Данных и Репозитория.
 */
class MyTaskFlowApplication : Application() {

    // Лениво создаем экземпляр Базы Данных
    private val database by lazy { AppDatabase.getDatabase(this) }

    // --- ИЗМЕНЕНИЕ ---
    // "Лениво" создаем Репозиторий, передавая ему ОБА DAO.
    val taskRepository by lazy {
        TaskRepository(database.taskDao(), database.subTaskDao())
    }
    // --- КОНЕЦ ИЗМЕНЕНИЯ ---
}