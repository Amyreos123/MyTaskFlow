package com.example.mytaskflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytaskflow.data.Habit
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    modifier: Modifier = Modifier,
    // 1. Получаем ViewModel, используя нашу Фабрику
    viewModel: HabitsViewModel = viewModel(factory = HabitsViewModel.Factory)
) {
    // 2. "Подписываемся" на поток привычек из ViewModel
    val habits by viewModel.allHabits.collectAsState(initial = emptyList())

    // 3. Получаем таймстэмп для "сегодня".
    //    Используем 'remember', чтобы эта функция не вызывалась
    //    при каждой "перерисовке" (recomposition).
    val todayTimestamp = remember { getTodayStartTimestamp() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Привычки") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // 4. Секция для добавления новой привычки
            AddHabitSection(
                onAddHabit = { habitTitle ->
                    viewModel.insertHabit(habitTitle)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Список всех привычек
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(habits, key = { it.id }) { habit ->
                    // 6. Проверяем, выполнена ли привычка СЕГОДНЯ
                    val isCompletedToday = habit.completedDates.contains(todayTimestamp)

                    HabitItem(
                        habit = habit,
                        isCompletedToday = isCompletedToday,
                        onHabitClick = {
                            // 7. При клике - просто сообщаем ViewModel.
                            //    ViewModel сам разберется с логикой.
                            viewModel.onHabitClicked(habit)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable-элемент для отображения одной привычки в списке.
 */
@Composable
fun HabitItem(
    habit: Habit,
    isCompletedToday: Boolean,
    onHabitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habit.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            // Checkbox "отмечает" выполнение за СЕГОДНЯ
            Checkbox(
                checked = isCompletedToday,
                // Важно: onCheckedChange принимает Boolean, но он нам не нужен,
                // т.к. ViewModel сам решает, какое будет состояние.
                // Поэтому мы просто вызываем onHabitClick().
                onCheckedChange = { onHabitClick() }
            )
        }
    }
}

/**
 * Composable-элемент для поля ввода и кнопки добавления привычки.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitSection(
    onAddHabit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Новая привычка") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done // Кнопка "Готово" на клавиатуре
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.isNotBlank()) {
                        onAddHabit(text)
                        text = ""
                        keyboardController?.hide()
                    }
                }
            )
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAddHabit(text)
                    text = ""
                    keyboardController?.hide()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить привычку")
        }
    }
}

/**
 * Вспомогательная функция, дублирующая логику из ViewModel,
 * чтобы UI мог определить, "отмечен" ли сегодняшний день.
 */
private fun getTodayStartTimestamp(): Long {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}