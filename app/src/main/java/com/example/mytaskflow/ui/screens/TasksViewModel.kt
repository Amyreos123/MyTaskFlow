package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mytaskflow.data.MyTaskFlowApplication
// import com.example.mytaskflow.data.SubTask <-- УДАЛЯЕМ
import com.example.mytaskflow.data.Task
import com.example.mytaskflow.data.TaskPriority
import com.example.mytaskflow.data.TaskRepository
import com.example.mytaskflow.data.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    // Тип потока <List<TaskWithSubTasks>> - это остается
    val allTasks: Flow<List<TaskWithSubTasks>> = repository.allTasks

    // --- Функции для Task ---
    fun insertTask(title: String, priority: TaskPriority) = viewModelScope.launch {
        if (title.isNotBlank()) {
            repository.insert(Task(title = title, priority = priority.value))
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    // --- УДАЛЯЕМ ЛОГИКУ ПОДЗАДАЧ ---
    // (Она переехала в TaskDetailViewModel)
    /*
    fun insertSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.insertSubTask(subTask)
    }

    fun updateSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.updateSubTask(subTask)
    }

    fun deleteSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.deleteSubTask(subTask)
    }
    */
    // --- КОНЕЦ УДАЛЕНИЯ ---


    // --- Фабрика (инструкция по сборке) ---
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyTaskFlowApplication)
                val repository = application.taskRepository
                return TasksViewModel(repository) as T
            }
        }
    }
}