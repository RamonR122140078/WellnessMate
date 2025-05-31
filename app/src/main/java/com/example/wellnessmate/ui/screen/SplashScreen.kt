package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wellnessmate.R
import com.example.wellnessmate.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    backgroundColor: Color = Color(0xFF4FC3F7), // Warna background biru, dikoreksi dengan format 0xFF
    textColor: Color = Color.Black // Warna text, dapat diganti sesuai kebutuhan
) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Login.route) {
            // Menghapus SplashScreen dari back stack
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Menggunakan R.drawable.logo sesuai dengan kode yang diberikan
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "WellnessMate Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "WellnessMate",
                style = MaterialTheme.typography.headlineMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}