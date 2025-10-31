package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mytaskflow.data.MyTaskFlowApplication
import com.example.mytaskflow.data.Task
import com.example.mytaskflow.data.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// 1. ViewModel принимает TaskRepository в конструкторе.
class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    // 2. Он "достает" Flow (поток) задач из репозитория.
    // UI (TasksScreen) будет "подписан" на этот поток.
    val allTasks: Flow<List<Task>> = repository.allTasks

    // 3. Функция для добавления новой задачи.
    // Она запускает Coroutine в viewModelScope (безопасный "контекст" для ViewModel).
    fun insertTask(title: String) = viewModelScope.launch {
        // Мы пока создаем простую задачу, в будущем здесь будет больше логики
        if (title.isNotBlank()) {
            repository.insert(Task(title = title))
        }
    }

    // 4. Функция для обновления задачи (например, для Checkbox).
    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    // --- Фабрика (инструкция по сборке) ---
    // Это стандартный шаблон, который нужно просто запомнить.
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // 1. Получаем Application (который мы создали)
                val application = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyTaskFlowApplication)

                // 2. Берем из него наш репозиторий
                val repository = application.taskRepository

                // 3. Создаем и возвращаем TasksViewModel
                return TasksViewModel(repository) as T
            }
        }
    }
}