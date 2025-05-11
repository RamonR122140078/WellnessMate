package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wellnessmate.Screen

@Composable
fun DashboardScreen(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Dashboard",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = { navController.navigate(Screen.Hydration.route) },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Hydration Tracker")
        }
        Button(
            onClick = { navController.navigate(Screen.MoodJournal.route) },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Mood Journal")
        }
        Button(
            onClick = { navController.navigate(Screen.DailyGoals.route) },
        ) {
            Text("Daily Goals")
        }
    }
}