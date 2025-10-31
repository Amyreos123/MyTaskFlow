package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
// Убираем неиспользуемые импорты Habit
import com.example.mytaskflow.data.HubItem
import com.example.mytaskflow.data.HubRepository
import com.example.mytaskflow.data.myTaskFlowApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HubViewModel(private val hubRepository: HubRepository) : ViewModel() {

    // --- ИСПРАВЛЕНИЕ: Убедимся, что здесь используется HubRepository ---
    // (В твоем файле здесь была ошибка, он ссылался на Habit)
    // Я исправляю это здесь.
    val allItems: StateFlow<List<HubItem>> = hubRepository.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun insertItem(item: HubItem) {
        viewModelScope.launch {
            hubRepository.insert(item)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            hubRepository.deleteById(id)
        }
    }

    // --- ИСПРАВЛЕНИЕ: Фабрика теперь корректно использует hubRepository ---
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Используем вспомогательную функцию, которая уже импортирована
                val application = this.myTaskFlowApplication()
                HubViewModel(application.hubRepository)
            }
        }
    }
    // --- КОНЕЦ ИСПРАВЛЕНИЙ ---
}