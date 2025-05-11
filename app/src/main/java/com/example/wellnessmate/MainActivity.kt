package com.example.wellnessmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.wellnessmate.ui.theme.WellnessMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WellnessMateTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController)
            }
        }
    }
}