package com.example.mytaskflow.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytaskflow.navigation.Screen
import com.example.mytaskflow.navigation.bottomNavigationItems
import com.example.mytaskflow.ui.screens.HabitsScreen
import com.example.mytaskflow.ui.screens.HomeScreen
import com.example.mytaskflow.ui.screens.HubScreen
import com.example.mytaskflow.ui.screens.TasksScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    // 1. Создаем NavController - он управляет навигацией
    val navController = rememberNavController()

    // 2. Scaffold - это "каркас" Material Design.
    // Он предоставляет слоты для TopBar, BottomBar, контента и т.д.
    Scaffold(
        bottomBar = {
            // 3. Наша нижняя панель навигации
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                // Получаем текущее местоположение в графе навигации
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // 4. Создаем по одной иконке для каждого экрана из нашего списка
                bottomNavigationItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.titleResId)) },
                        // Проверяем, совпадает ли маршрут иконки с текущим маршрутом
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            // 5. Логика нажатия
                            navController.navigate(screen.route) {
                                // Этот код нужен, чтобы не создавать копии экранов
                                // при повторном нажатии на ту же иконку
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
        // 6. NavHost - это контейнер, где будут отображаться наши экраны
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Начинаем с "Главной"
            modifier = Modifier.padding(innerPadding) // Отступ от нижней панели
        ) {
            // 7. Определяем, какой Composable соответствует какому маршруту
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Habits.route) { HabitsScreen() }
            composable(Screen.Hub.route) { HubScreen() }
        }
    }
}