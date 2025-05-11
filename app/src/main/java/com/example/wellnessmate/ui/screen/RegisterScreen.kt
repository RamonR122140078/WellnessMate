package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wellnessmate.Screen

@Composable
fun RegisterScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Register", style = MaterialTheme.typography.displayMedium)
        Button(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Daftar")
        }
    }
}