package com.example.mytaskflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytaskflow.navigation.Screen
import com.example.mytaskflow.ui.screens.HabitsScreen
import com.example.mytaskflow.ui.screens.HomeScreen
import com.example.mytaskflow.ui.screens.HubScreen
import com.example.mytaskflow.ui.screens.TasksScreen

// Обновленный список для навигации
val bottomNavItems = listOf(
    BottomNavItem(
        label = "Главная",
        icon = Icons.Filled.Home,
        screen = Screen.Home
    ),
    BottomNavItem(
        label = "Задачи",
        icon = Icons.Filled.List,
        screen = Screen.Tasks
    ),
    BottomNavItem(
        label = "Привычки",
        icon = Icons.Filled.Star,
        screen = Screen.Habits
    ),
    BottomNavItem(
        label = "Хаб",
        icon = Icons.Filled.DateRange, // Используем другую иконку для примера
        screen = Screen.Hub
    )
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(modifier = Modifier) }
            composable(Screen.Tasks.route) {
                //
                // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
                //
                // Вызываем TasksScreen БЕЗ viewModel
                TasksScreen(modifier = Modifier)
            }
            composable(Screen.Habits.route) { HabitsScreen(modifier = Modifier) }
            composable(Screen.Hub.route) { HubScreen(modifier = Modifier) }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)