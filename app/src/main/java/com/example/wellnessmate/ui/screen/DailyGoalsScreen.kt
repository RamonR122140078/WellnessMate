package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// Removed LocalDrink import - using emoji instead
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wellnessmate.data.model.Goal
import com.example.wellnessmate.ui.viewmodel.DailyGoalsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyGoalsScreen(
    navController: NavHostController,
    viewModel: DailyGoalsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var waterProgress = remember { mutableIntStateOf(0) }
    val goals by viewModel.allGoals.collectAsState()

    // Modern color scheme
    val primaryColor = Color(0xFF2196F3)
    val secondaryColor = Color(0xFF03DAC6)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8F9FA),
            Color(0xFFE9ECEF)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Header Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "🎯 Target Harian",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Atur dan pantau pencapaian target harianmu",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6C757D)
                        )
                    }
                }
            }

            item {
                WaterIntakeTracker(
                    currentValue = waterProgress.intValue,
                    onValueChange = { waterProgress.intValue = it },
                    onSave = {
                        viewModel.updateWaterGoal(waterProgress.intValue)
                        waterProgress.intValue = 0
                    },
                    primaryColor = primaryColor
                )
            }

            item {
                GoalsList(
                    goals = goals,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor
                )
            }
        }
    }
}

@Composable
fun WaterIntakeTracker(
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    onSave: () -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(primaryColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💧",
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Target Air Minum",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Rekomendasi: 2000 ml/hari",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6C757D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8F9FA))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress Saat Ini:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6C757D)
                    )
                    Text(
                        text = "$currentValue ml",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Slider
            Text(
                text = "Atur jumlah air (ml)",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = currentValue.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = 0f..2000f,
                steps = 39, // 50ml increments
                colors = SliderDefaults.colors(
                    thumbColor = primaryColor,
                    activeTrackColor = primaryColor,
                    inactiveTrackColor = primaryColor.copy(alpha = 0.3f)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "0 ml",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6C757D)
                )
                Text(
                    text = "2000 ml",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6C757D)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Simpan Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun GoalsList(
    goals: List<Goal>,
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "📊 Semua Target",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8F9FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎯",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Belum ada target yang diset",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6C757D)
                        )
                    }
                }
            } else {
                goals.forEach { goal ->
                    GoalItem(
                        goal = goal,
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor
                    )
                    if (goal != goals.last()) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun GoalItem(
    goal: Goal,
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = (goal.currentValue.toFloat() / goal.targetValue).coerceIn(0f, 1f)
    val isCompleted = progress >= 1f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) secondaryColor.copy(alpha = 0.1f) else Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = goal.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6C757D)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${goal.currentValue}/${goal.targetValue} ${goal.unit}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) secondaryColor else primaryColor
                    )

                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6C757D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE9ECEF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isCompleted) {
                                Brush.horizontalGradient(
                                    colors = listOf(secondaryColor, secondaryColor.copy(alpha = 0.8f))
                                )
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(primaryColor, primaryColor.copy(alpha = 0.8f))
                                )
                            }
                        )
                )
            }

            if (isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = secondaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Target tercapai!",
                        style = MaterialTheme.typography.bodySmall,
                        color = secondaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}