package com.wish.spideplan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wish.spideplan.ui.screen.HomeScreen
import com.wish.spideplan.ui.viewmodel.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Long) = "edit_task/$taskId"
    }
    object Sleep : Screen("sleep")
    object BrainDump : Screen("brain_dump")
    object Splash : Screen("splash")
}

@Composable
fun SpidePlanNavigation(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddTask.route)
                },
                onNavigateToSleep = {
                    navController.navigate(Screen.Sleep.route)
                },
                onNavigateToBrainDump = {
                    navController.navigate(Screen.BrainDump.route)
                }
            )
        }
        
        composable(Screen.AddTask.route) {
            // AddTaskScreen will be implemented
            // For now, navigate back
            navController.popBackStack()
        }
        
        composable(Screen.EditTask.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull() ?: 0L
            // EditTaskScreen will be implemented
            // For now, navigate back
            navController.popBackStack()
        }
        
        composable(Screen.Sleep.route) {
            // SleepScreen will be implemented
            // For now, navigate back
            navController.popBackStack()
        }
        
        composable(Screen.BrainDump.route) {
            // BrainDumpScreen will be implemented
            // For now, navigate back
            navController.popBackStack()
        }
    }
}