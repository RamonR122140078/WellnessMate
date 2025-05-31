package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wellnessmate.Screen
import androidx.navigation.NavHostController

@Composable
fun DashboardScreen(
    navController: NavHostController,
    backgroundColor: Color = Color(0xFF4FC3F7),
    buttonColor: Color = Color(0xFF0288D1)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    color = buttonColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = { navController.navigate(Screen.Hydration.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hydration Tracker", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { navController.navigate(Screen.MoodJournal.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Mood Journal", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { navController.navigate(Screen.DailyGoals.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Daily Goals", fontWeight = FontWeight.Bold)
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = Color.LightGray
                )

                // Settings Button
                TextButton(
                    onClick = { navController.navigate(Screen.Settings.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Pengaturan Profil",
                        textAlign = TextAlign.Center,
                        color = buttonColor
                    )
                }
            }
        }
    }
}
