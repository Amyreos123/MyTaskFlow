package com.example.mytaskflow.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mytaskflow.R

// Sealed class (запечатанный класс) ограничивает иерархию.
// Мы говорим: "Существуют ТОЛЬКО эти четыре экрана".
sealed class Screen(
    val route: String, // Уникальный ID для навигации
    @StringRes val titleResId: Int, // Ссылка на наш strings.xml
    val icon: ImageVector // Иконка
) {
    // object (объект) используется, так как у нас только один экземпляр каждого экрана
    data object Home : Screen(
        route = "home",
        titleResId = R.string.nav_title_home,
        icon = Icons.Filled.Home
    )

    data object Tasks : Screen(
        route = "tasks",
        titleResId = R.string.nav_title_tasks,
        icon = Icons.Filled.Checklist
    )

    data object Habits : Screen(
        route = "habits",
        titleResId = R.string.nav_title_habits,
        icon = Icons.Filled.Star
    )

    data object Hub : Screen(
        route = "hub",
        titleResId = R.string.nav_title_hub,
        icon = Icons.Filled.Link
    )
}

// Список всех наших экранов для удобного использования в навигации
val bottomNavigationItems = listOf(
    Screen.Home,
    Screen.Tasks,
    Screen.Habits,
    Screen.Hub
)