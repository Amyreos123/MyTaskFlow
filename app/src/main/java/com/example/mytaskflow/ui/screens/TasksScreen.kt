package com.example.mytaskflow.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytaskflow.data.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier
) {
    // Получаем ViewModel
    val viewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)
    // "Подписываемся" на список задач
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())

    // Состояния для модального окна
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var taskName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Задачи") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить задачу"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Используем items(tasks, key = { it.id })
                // key = { it.id } - это важно для анимаций.
                // Compose будет знать, какой элемент удалился, по его 'id'.
                items(tasks, key = { it.id }) { task ->
                    // --- НОВОЕ: SwipeToDismissBox ---
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            // Этот код выполняется, когда свайп завершен
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                // Если свайпнули справа налево (EndToStart)
                                viewModel.deleteTask(task)
                                true // Подтверждаем удаление
                            } else {
                                false // В других случаях (свайп налево) не делаем ничего
                            }
                        },
                        // Мы отключаем свайп слева направо (StartToEnd)
                        positionalThreshold = { it * 0.25f }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        // Фон, который появляется ПОД карточкой во время свайпа
                        backgroundContent = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                                    else -> Color.Transparent
                                }, label = "color"
                            )
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.2f else 0.8f,
                                label = "scale"
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd // Иконка будет справа
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    modifier = Modifier.scale(scale),
                                    tint = Color.White
                                )
                            }
                        },
                        // Мы разрешаем свайп только в одном направлении (справа налево)
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        // --- КОНЕЦ НОВОГО ---

                        // Наш TaskItem, который мы тоже обновим
                        TaskItem(
                            task = task,
                            onTaskCheckedChange = { isChecked ->
                                // Обновляем задачу, копируя ее с новым 'isCompleted'
                                viewModel.updateTask(task.copy(isCompleted = isChecked))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                AddTaskBottomSheet(
                    taskName = taskName,
                    onTaskNameChange = { newName ->
                        taskName = newName
                    },
                    onSaveTask = {
                        if (taskName.isNotBlank()) {
                            viewModel.insertTask(taskName)
                            taskName = ""
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Composable-элемент для отображения одной задачи в списке.
 * (Мы вернули Checkbox и логику)
 */
@Composable
fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                // --- НОВОЕ: Стилизация текста ---
                // Если задача выполнена, перечеркиваем текст
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
            )
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onTaskCheckedChange
            )
        }
    }
}