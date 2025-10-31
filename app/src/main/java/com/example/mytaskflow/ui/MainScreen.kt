package com.example.mytaskflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytaskflow.navigation.Screen
import com.example.mytaskflow.ui.screens.HabitsScreen
import com.example.mytaskflow.ui.screens.HomeScreen
import com.example.mytaskflow.ui.screens.HubScreen
import com.example.mytaskflow.ui.screens.TaskDetailScreen
import com.example.mytaskflow.ui.screens.TasksScreen

// Data class для описания элемента нижней навигации
data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Список наших экранов для нижней панели
    val navItems = listOf(
        NavItem("Главная", Icons.Default.Home, Screen.Home.route),
        NavItem("Задачи", Icons.Default.List, Screen.Tasks.route),
        NavItem("Привычки", Icons.Default.DateRange, Screen.Habits.route),
        NavItem("Хаб", Icons.Default.Settings, Screen.Hub.route) // Используем Settings пока нет иконки "Хаб"
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { navItem ->
                    NavigationBarItem(
                        icon = { Icon(navItem.icon, contentDescription = navItem.title) },
                        label = { Text(navItem.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                // Этот код гарантирует, что мы не будем "накапливать"
                                // экраны в стеке при переключении вкладок.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Tasks.route) {
                TasksScreen(
                    onTaskClick = { taskId ->
                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                    }
                )
            }
            composable(Screen.Habits.route) {
                HabitsScreen()
            }
            composable(Screen.Hub.route) {
                HubScreen()
            }

            // Экран "Детали задачи" с аргументом
            composable(
                route = Screen.TaskDetail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) { backStackEntry ->

                // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
                // Было: onNavigateBack = { navController.popBackStack() }
                // Стало: onNavigateUp = { navController.popBackStack() }
                TaskDetailScreen(
                    onNavigateUp = { navController.popBackStack() }
                )
                // --- КОНЕЦ ИСПРАВЛЕНИЯ ---
            }
        }
    }
}