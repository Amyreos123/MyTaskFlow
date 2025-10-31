package com.example.mytaskflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
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
// Импортируем только те ViewModel, которые нам нужны здесь
import com.example.mytaskflow.ui.screens.TaskDetailViewModel

// Элементы для нижней навигации
val navBarItems = listOf(
    NavBarItem("Главная", Icons.Default.Home, Screen.Home.route),
    NavBarItem("Задачи", Icons.Default.List, Screen.Tasks.route),
    NavBarItem("Привычки", Icons.Default.CheckBox, Screen.Habits.route),
    NavBarItem("Хаб", Icons.Default.DataObject, Screen.Hub.route)
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navBarItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
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
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Главный экран
            composable(Screen.Home.route) {
                HomeScreen()
            }

            // 2. Экран Задач
            composable(Screen.Tasks.route) {
                TasksScreen(
                    onTaskClick = { taskId ->
                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                    }
                )
            }

            // 3. Экран Привычек
            composable(Screen.Habits.route) {
                HabitsScreen()
            }

            // 4. Экран Хаб
            composable(Screen.Hub.route) {
                HubScreen()
            }

            // 5. Экран Деталей Задачи
            composable(
                route = Screen.TaskDetail.route,
                arguments = listOf(navArgument("taskId") {
                    type = NavType.LongType
                })
            ) {
                // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
                // Меняем 'onNavigateBack' на 'onNavigateUp',
                // чтобы соответствовать TaskDetailScreen.kt
                TaskDetailScreen(
                    viewModel = viewModel(factory = TaskDetailViewModel.Factory),
                    onNavigateUp = { navController.popBackStack() }
                )
                // --- КОНЕЦ ИСПРАВЛЕНИЯ ---
            }
        }
    }
}

data class NavBarItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)