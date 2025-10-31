package com.example.mytaskflow.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mytaskflow.data.MyTaskFlowApplication
import com.example.mytaskflow.data.SubTask
import com.example.mytaskflow.data.Task
import com.example.mytaskflow.data.TaskRepository
import com.example.mytaskflow.data.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана TaskDetailScreen.
 *
 * @param repository Репозиторий для доступа к данным.
 * @param savedStateHandle "Контейнер", который хранит состояние ViewModel,
 * а также АРГУМЕНТЫ НАВИГАЦИИ (наш taskId).
 */
class TaskDetailViewModel(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 1. Получаем 'taskId' из 'savedStateHandle'.
    //    Имя "taskId" должно совпадать с тем, что мы указали в Screen.kt
    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    // 2. "Подписываемся" на Flow<TaskWithSubTasks> из репозитория
    //    используя полученный taskId.
    //    .stateIn() превращает холодный Flow в горячий StateFlow,
    //    который "живет", пока жив ViewModel (viewModelScope),
    //    и кэширует последнее значение.
    val taskWithSubTasks: StateFlow<TaskWithSubTasks?> = repository.getTaskWithSubTasks(taskId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // --- Логика, которую мы перенесли из TasksViewModel ---

    fun insertSubTask(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) {
            // Мы используем taskId (который мы получили из savedStateHandle)
            // для создания SubTask с правильным внешним ключом.
            repository.insertSubTask(SubTask(taskId = taskId, title = title))
        }
    }

    fun updateSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.updateSubTask(subTask)
    }

    fun deleteSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.deleteSubTask(subTask)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    // --- Фабрика (инструкция по сборке) ---
    // Эта Фабрика немного сложнее, т.к. ViewModel принимает
    // ДВА параметра: Repository и SavedStateHandle.
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // 1. Получаем Application
                val application = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyTaskFlowApplication)
                // 2. Получаем Репозиторий
                val repository = application.taskRepository
                // 3. Получаем SavedStateHandle
                val savedStateHandle = extras.createSavedStateHandle()

                // 4. Создаем ViewModel, передавая все зависимости
                return TaskDetailViewModel(repository, savedStateHandle) as T
            }
        }
    }
}