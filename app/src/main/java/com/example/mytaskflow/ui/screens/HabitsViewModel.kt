package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytaskflow.data.Habit
import com.example.mytaskflow.data.HabitRepository
import com.example.mytaskflow.data.HubItem
import com.example.mytaskflow.data.HubRepository
import com.example.mytaskflow.data.myTaskFlowApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HubViewModel(private val hubRepository: HubRepository) : ViewModel() {

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

    // --- ЭТО ИСПРАВЛЕНИЕ ВЫЛЕТА ---
    // Исправляем неправильное получение Application
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