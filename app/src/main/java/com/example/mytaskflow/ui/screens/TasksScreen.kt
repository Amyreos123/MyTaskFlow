package com.example.mytaskflow.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytaskflow.R
import com.example.mytaskflow.data.Task

@Composable
fun TasksScreen(modifier: Modifier = Modifier) {

    // 1. Получаем экземпляр ViewModel, используя нашу Фабрику
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)

    // 2. "Собираем" задачи из Flow.
    // `collectAsState` превращает Flow в State.
    // Как только в БД что-то изменится, `tasks` обновится, и Compose перерисует экран.
    val tasks by tasksViewModel.allTasks.collectAsState(initial = emptyList())

    // 3. Используем Column для вертикального расположения элементов
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Заголовок
        Text(
            text = stringResource(id = R.string.nav_title_tasks),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Временная кнопка для добавления тестовой задачи
        Button(
            onClick = { tasksViewModel.insertTask("Новая тестовая задача") },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Добавить задачу")
        }

        // 4. LazyColumn - это "умный" список (как RecyclerView).
        // Он создает элементы только для тех задач, которые видны на экране.
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(tasks) { task ->
                // Для каждой задачи в списке `tasks` создаем TaskItem
                TaskItem(
                    task = task,
                    onTaskCheckedChange = { isChecked ->
                        // Когда пользователь нажимает Checkbox,
                        // мы обновляем задачу в ViewModel
                        tasksViewModel.updateTask(task.copy(isCompleted = isChecked))
                    }
                )
            }
        }
    }
}

// Это Composable-функция для одного элемента списка
@Composable
fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onTaskCheckedChange
        )
    }
}