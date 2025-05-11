package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wellnessmate.ui.viewmodel.DailyGoalsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun DailyGoalsScreen(viewModel: DailyGoalsViewModel = viewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Target Harian", style = MaterialTheme.typography.displayMedium)

        var waterProgress by remember { mutableStateOf(0) }

        Text("Target Air: 2000 ml")
        Slider(
            value = waterProgress.toFloat(),
            onValueChange = { waterProgress = it.toInt() },
            valueRange = 0f..2000f
        )
        Text("$waterProgress ml")

        Button(onClick = { viewModel.updateWaterGoal(waterProgress) }) {
            Text("Simpan Progress")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Semua Target:")
        val goals by viewModel.allGoals.collectAsState(initial = emptyList())
        goals.forEach {
            Text("${it.goalType}: ${it.currentValue}/${it.targetValue}")
        }
    }
}