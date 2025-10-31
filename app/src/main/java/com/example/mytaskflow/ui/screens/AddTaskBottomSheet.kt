package com.example.mytaskflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mytaskflow.R

/**
 * Composable для содержимого модального нижнего окна добавления задачи.
 *
 * @param taskName Текущее значение текста в поле ввода.
 * @param onTaskNameChange Функция обратного вызова при изменении текста.
 * @param onSaveTask Функция обратного вызова при нажатии кнопки "Сохранить".
 */
@Composable
fun AddTaskBottomSheet(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    onSaveTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            // safeDrawingPadding() добавляет отступы, чтобы контент не заходил
            // под системные элементы, например, навигационную панель.
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

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSaveTask,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}