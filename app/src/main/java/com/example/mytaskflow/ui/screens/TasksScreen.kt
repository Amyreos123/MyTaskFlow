package com.example.mytaskflow.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
// --- НОВОЕ ---
import androidx.compose.foundation.clickable
// --- КОНЕЦ НОВОГО ---
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
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
import com.example.mytaskflow.data.TaskPriority
import com.example.mytaskflow.data.TaskWithSubTasks
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    // --- НОВОЕ ---
    // 1. Принимаем лямбду onTaskClick, которая ожидает Int (taskId)
    onTaskClick: (Int) -> Unit,
    // --- КОНЕЦ НОВОГО ---
    modifier: Modifier = Modifier
) {
    val viewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var taskName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var selectedPriority by remember { mutableStateOf(TaskPriority.NORMAL) }


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
                items(tasks, key = { it.task.id }) { taskWithSubTasks ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTask(taskWithSubTasks.task)
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { it * 0.25f }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val color by animateColorAsState(
                                targetValue = when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                                    else -> Color.Transparent
                                },
                                label = "color"
                            )
                            val scale by animateFloatAsState(
                                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.2f else 0.8f,
                                label = "scale"
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    modifier = Modifier.scale(scale),
                                    tint = Color.White
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        TaskItem(
                            taskWithSubTasks = taskWithSubTasks,
                            onTaskCheckedChange = { isChecked ->
                                viewModel.updateTask(
                                    taskWithSubTasks.task.copy(isCompleted = isChecked)
                                )
                            },
                            // --- НОВОЕ ---
                            // 2. Передаем лямбду onTaskClick в TaskItem
                            onTaskClick = {
                                onTaskClick(taskWithSubTasks.task.id)
                            },
                            // --- КОНЕЦ НОВОГО ---
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
                    selectedPriority = selectedPriority,
                    onPriorityChange = { newPriority ->
                        selectedPriority = newPriority
                    },
                    onSaveTask = {
                        if (taskName.isNotBlank()) {
                            viewModel.insertTask(taskName, selectedPriority)
                            taskName = ""
                            selectedPriority = TaskPriority.NORMAL
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
 */
@Composable
fun TaskItem(
    taskWithSubTasks: TaskWithSubTasks,
    onTaskCheckedChange: (Boolean) -> Unit,
    // --- НОВОЕ ---
    // 3. Принимаем лямбду onTaskClick
    onTaskClick: () -> Unit,
    // --- КОНЕЦ НОВОГО ---
    modifier: Modifier = Modifier
) {
    val task = taskWithSubTasks.task
    val subTasks = taskWithSubTasks.subTasks
    val priority = TaskPriority.fromValue(task.priority)

    val priorityBorder = when (priority) {
        TaskPriority.HIGH -> BorderStroke(2.dp, MaterialTheme.colorScheme.error)
        TaskPriority.LOW -> BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        TaskPriority.NORMAL -> null
    }

    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            // --- НОВОЕ ---
            // 4. Делаем Card кликабельной
            .clickable { onTaskClick() },
        // --- КОНЕЦ НОВОГО ---
        border = priorityBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                )
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onTaskCheckedChange
                )
            }

            if (subTasks.isNotEmpty()) {
                val completedCount = subTasks.count { it.isCompleted }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Подзадачи",
                        modifier = Modifier.padding(end = 6.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$completedCount / ${subTasks.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}