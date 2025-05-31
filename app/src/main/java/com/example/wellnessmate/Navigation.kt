package com.example.wellnessmate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wellnessmate.ui.screen.DailyGoalsScreen
import com.example.wellnessmate.ui.screen.DashboardScreen
import com.example.wellnessmate.ui.screen.HydrationScreen
import com.example.wellnessmate.ui.screen.LoginScreen
import com.example.wellnessmate.ui.screen.MoodJournalScreen
import com.example.wellnessmate.ui.screen.RegisterScreen
import com.example.wellnessmate.ui.screen.SettingsScreen
import com.example.wellnessmate.ui.screen.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Hydration : Screen("hydration")
    object MoodJournal : Screen("mood")
    object DailyGoals : Screen("goals")
    object Settings : Screen("settings")
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Hydration.route) { HydrationScreen(navController) }
        composable(Screen.MoodJournal.route) { MoodJournalScreen(navController) }
        composable(Screen.DailyGoals.route) { DailyGoalsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }
}