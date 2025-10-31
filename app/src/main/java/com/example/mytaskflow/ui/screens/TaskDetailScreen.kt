package com.example.mytaskflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytaskflow.data.SubTask
import com.example.mytaskflow.data.Task
import com.example.mytaskflow.data.TaskPriority
import com.example.mytaskflow.data.TaskWithSubTasks

/**
 * Главный Composable для экрана деталей задачи.
 *
 * @param onNavigateUp Функция обратного вызова для навигации "назад".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    // 1. Получаем ViewModel, используя нашу новую Фабрику
    viewModel: TaskDetailViewModel = viewModel(factory = TaskDetailViewModel.Factory)
) {
    // 2. "Подписываемся" на StateFlow.
    //    taskWithSubTasks может быть null, пока идет загрузка
    val taskWithSubTasks by viewModel.taskWithSubTasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали задачи") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // 3. Проверяем, загрузилась ли задача
        if (taskWithSubTasks == null) {
            // Показываем индикатор загрузки, если данных еще нет
            CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
        } else {
            // 4. Если данные есть, отображаем их
            // (используем '!' т.к. мы уверены, что taskWithSubTasks не null)
            TaskDetailContent(
                taskWithSubTasks = taskWithSubTasks!!,
                onUpdateTask = viewModel::updateTask,
                onInsertSubTask = viewModel::insertSubTask,
                onUpdateSubTask = viewModel::updateSubTask,
                onDeleteSubTask = viewModel::deleteSubTask,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

/**
 * Composable, отвечающий за отображение контента,
 * когда задача и подзадачи уже загружены.
 */
@Composable
private fun TaskDetailContent(
    taskWithSubTasks: TaskWithSubTasks,
    onUpdateTask: (Task) -> Unit,
    onInsertSubTask: (String) -> Unit,
    onUpdateSubTask: (SubTask) -> Unit,
    onDeleteSubTask: (SubTask) -> Unit,
    modifier: Modifier = Modifier
) {
    val task = taskWithSubTasks.task
    val subTasks = taskWithSubTasks.subTasks

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- 1. Отображение главной задачи ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    onUpdateTask(task.copy(isCompleted = isChecked))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            )
        }
        Text(
            text = "Приоритет: ${TaskPriority.fromValue(task.priority).displayName}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 48.dp) // Отступ, как у Checkbox + Spacer
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Шаги выполнения:",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- 2. Поле для добавления новой подзадачи ---
        AddSubTaskInput(onInsertSubTask = onInsertSubTask)

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. Список подзадач ---
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(subTasks, key = { it.id }) { subTask ->
                SubTaskItem(
                    subTask = subTask,
                    onSubTaskChecked = { isChecked ->
                        onUpdateSubTask(subTask.copy(isCompleted = isChecked))
                    },
                    onSubTaskDelete = {
                        onDeleteSubTask(subTask)
                    }
                )
            }
        }
    }
}

/**
 * Composable для одного элемента (подзадачи) в списке.
 */
@Composable
private fun SubTaskItem(
    subTask: SubTask,
    onSubTaskChecked: (Boolean) -> Unit,
    onSubTaskDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = subTask.isCompleted,
                onCheckedChange = onSubTaskChecked
            )
            Text(
                text = subTask.title,
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (subTask.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = onSubTaskDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить шаг",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Composable для поля ввода новой подзадачи.
 */
@Composable
private fun AddSubTaskInput(
    onInsertSubTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier.fillMaxWidth(),
        label = { Text("Новый шаг...") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done // Кнопка "Готово" на клавиатуре
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // При нажатии "Готово":
                if (text.isNotBlank()) {
                    onInsertSubTask(text) // 1. Сохраняем
                    text = "" // 2. Очищаем поле
                    keyboardController?.hide() // 3. Прячем клавиатуру
                }
            }
        )
    )
}