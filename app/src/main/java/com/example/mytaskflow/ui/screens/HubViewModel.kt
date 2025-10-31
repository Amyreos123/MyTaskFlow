package com.example.mytaskflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
// --- ИСПРАВЛЕНИЕ: Убираем неиспользуемые импорты ---
// import com.example.mytaskflow.data.Habit
// import com.example.mytaskflow.data.HabitRepository
// --- КОНЕЦ ИСПРАВЛЕНИЯ ---
import com.example.mytaskflow.data.HubItem
import com.example.mytaskflow.data.HubRepository
import com.example.mytaskflow.data.myTaskFlowApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HubViewModel(private val hubRepository: HubRepository) : ViewModel() {

    // --- ИСПРАВЛЕНИЕ: ВОЗВРАЩАЕМ СКОБКИ () ---
    // В твоем HubRepository.kt (который ты прислал) allItems - это
    // функция fun getAllItems(), а не свойство.
    // Моя предыдущая инструкция убрать скобки была неверной.
    // Мы возвращаем как было:
    val allItems: StateFlow<List<HubItem>> = hubRepository.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    // --- КОНЕЦ ИСПРАВЛЕНИЯ ---

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

    // Эта фабрика у тебя работает правильно.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Используем вспомогательную функцию, которая уже импортирована
                val application = this.myTaskFlowApplication()
                HubViewModel(application.hubRepository)
            }
        }
    }
}