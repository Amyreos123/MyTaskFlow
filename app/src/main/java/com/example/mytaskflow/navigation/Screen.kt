package com.example.mytaskflow.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

// --- ИЗМЕНЕНИЕ ---
// 1. Делаем sealed class (запечатанный класс), чтобы он мог
//    содержать как 'object' (экраны без аргументов),
//    так и 'object' с доп. логикой (TaskDetail).
sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    // 2. Объекты для экранов без аргументов
    object Home : Screen("home")
    object Tasks : Screen("tasks")
    object Habits : Screen("habits")
    object Hub : Screen("hub")

    // --- НОВОЕ ---
    // 3. Создаем объект для экрана Деталей Задачи
    object TaskDetail : Screen(
        // 4. Маршрут теперь включает "заполнитель" {taskId}
        route = "taskDetail/{taskId}",
        // 5. Мы объявляем, что {taskId} - это аргумент
        //    типа NavType.IntType
        arguments = listOf(
            navArgument("taskId") {
                type = NavType.IntType
            }
        )
    ) {
        // 6. Вспомогательная функция, чтобы построить
        //    маршрут с реальным ID (например, "taskDetail/5")
        fun createRoute(taskId: Int) = "taskDetail/$taskId"
    }
    // --- КОНЕЦ НОВОГО ---
}