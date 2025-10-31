package com.example.mytaskflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
// --- ИЗМЕНЕНИЕ ---
// 1. Импортируем AutoMirrored.Filled.List
import androidx.compose.material.icons.automirrored.filled.List
// --- КОНЕЦ ИЗМЕНЕНИЯ ---
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
// import androidx.compose.material.icons.filled.List // 2. Удаляем или комментируем старый импорт
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytaskflow.navigation.Screen
import com.example.mytaskflow.ui.screens.HabitsScreen
import com.example.mytaskflow.ui.screens.HomeScreen
import com.example.mytaskflow.ui.screens.HubScreen
import com.example.mytaskflow.ui.screens.TaskDetailScreen
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
        // --- ИЗМЕНЕНИЕ ---
        // 3. Используем новую иконку
        icon = Icons.AutoMirrored.Filled.List,
        // --- КОНЕЦ ИЗМЕНЕНИЯ ---
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
    // 1. Переименовываем navController, чтобы сделать его более явным
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            // 2. Передаем NavController в BottomBar
            AppBottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        // 3. Передаем NavController и padding в NavHost
        AppNavHost(
            navController = bottomNavController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Composable для AppNavHost (Граф навигации).
 * Мы вынесли его для чистоты кода.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // --- Экраны нижнего меню ---
        composable(Screen.Home.route) {
            HomeScreen(modifier = Modifier)
        }
        composable(Screen.Tasks.route) {
            // 4. TasksScreen теперь получает лямбду onTaskClick
            TasksScreen(
                onTaskClick = { taskId ->
                    // 5. При клике - переходим на новый маршрут
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                modifier = Modifier
            )
        }
        composable(Screen.Habits.route) {
            HabitsScreen(modifier = Modifier)
        }
        composable(Screen.Hub.route) {
            HubScreen(modifier = Modifier)
        }

        // --- НОВОЕ: Экран Деталей ---
        // 6. Добавляем новый 'composable' для экрана деталей.
        composable(
            route = Screen.TaskDetail.route, // "taskDetail/{taskId}"
            arguments = Screen.TaskDetail.arguments // [navArgument("taskId")]
        ) {
            // 7. 'it' (NavBackStackEntry) здесь содержит 'taskId'
            //    TaskDetailViewModel автоматически получит его
            //    через SavedStateHandle.
            TaskDetailScreen(
                // 8. Для кнопки "Назад" мы просто вызываем 'navigateUp'
                onNavigateUp = { navController.navigateUp() }
            )
        }
        // --- КОНЕЦ НОВОГО ---
    }
}

/**
 * Composable для BottomNavigationBar
 */
@Composable
fun AppBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
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


data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)