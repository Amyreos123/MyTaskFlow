package com.example.mytaskflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytaskflow.data.TaskPriority

/**
 * Composable для содержимого модального нижнего окна добавления задачи.
 *
 * @param taskName Текущее значение текста в поле ввода.
 * @param onTaskNameChange Функция обратного вызова при изменении текста.
 *
 * --- НОВЫЕ ПАРАМЕТРЫ ---
 * @param selectedPriority Текущий выбранный приоритет.
 * @param onPriorityChange Функция обратного вызова при выборе другого приоритета.
 * --- КОНЕЦ НОВЫХ ПАРАМЕТРОВ ---
 *
 * @param onSaveTask Функция обратного вызова при нажатии кнопки "Сохранить".
 */
@OptIn(ExperimentalMaterial3Api::class) // Нужен для FilterChip
@Composable
fun AddTaskBottomSheet(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    // --- НОВОЕ ---
    selectedPriority: TaskPriority,
    onPriorityChange: (TaskPriority) -> Unit,
    // --- КОНЕЦ НОВОГО ---
    onSaveTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Новая задача",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = taskName,
            onValueChange = onTaskNameChange,
            label = { Text("Название задачи") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // --- НОВОЕ: Выбор приоритета ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Динамически создаем "чипы" для каждого приоритета из TaskPriority.entries
            TaskPriority.entries.forEach { priority ->
                FilterChip(
                    selected = (selectedPriority == priority),
                    onClick = { onPriorityChange(priority) },
                    label = { Text(priority.displayName) }
                )
            }
        }
        // --- КОНЕЦ НОВОГО ---

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSaveTask,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}