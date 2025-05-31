package com.example.wellnessmate.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wellnessmate.R
import com.example.wellnessmate.data.entity.HydrationEntity
import com.example.wellnessmate.ui.viewmodel.HydrationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HydrationScreen(
    navController: NavHostController,
    viewModel: HydrationViewModel = viewModel(),
    backgroundColor: Color = Color(0xFF4FC3F7),
    accentColor: Color = Color(0xFF0288D1)
) {
    val mainBackgroundColor = backgroundColor
    val cardBackgroundColor = Color.White
    val buttonColor = accentColor

    var showCustomAmountDialog by remember { mutableStateOf(false) }
    var selectedTimeFilter by remember { mutableStateOf("Hari Ini") }
    var dailyGoal by remember { mutableStateOf(2000) } // Default 2L goal

    // Get data from viewModel
    val entries by viewModel.allEntries.collectAsState(initial = emptyList())
    val today by viewModel.totalToday.collectAsState(initial = 0)

    // Calculate streak and statistics
    val streak = calculateStreak(entries, dailyGoal)
    val weeklyAverage = calculateWeeklyAverage(entries)
    val progress = ((today ?: 0).toFloat() / dailyGoal.toFloat()).coerceAtMost(1f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "WellnessMate Logo",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hidrasi",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                ),
                actions = {
                    Text(
                        text = "⚙️",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable { /* Open settings */ }
                            .padding(16.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCustomAmountDialog = true },
                containerColor = buttonColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Custom")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor)
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Progress Card
                        HydrationProgressCard(
                            currentAmount = today ?: 0,  // Use 0 if today is null
                            goalAmount = dailyGoal,
                            progress = progress,
                            cardBackgroundColor = cardBackgroundColor,
                            accentColor = accentColor
                        )
                    }

                    item {
                        // Quick Add Buttons
                        QuickAddButtonsCard(
                            onAddWater = { amount -> viewModel.addWater(amount) },
                            cardBackgroundColor = cardBackgroundColor,
                            buttonColor = buttonColor
                        )
                    }

                    item {
                        // Statistics Card
                        StatisticsCard(
                            streak = streak,
                            weeklyAverage = weeklyAverage,
                            cardBackgroundColor = cardBackgroundColor,
                            accentColor = accentColor
                        )
                    }

                    item {
                        // Time Filter
                        TimeFilterCard(
                            selectedFilter = selectedTimeFilter,
                            onFilterChange = { selectedTimeFilter = it },
                            cardBackgroundColor = cardBackgroundColor
                        )
                    }

                    item {
                        // History Card
                        HydrationHistoryCard(
                            entries = getFilteredEntries(entries, selectedTimeFilter),
                            cardBackgroundColor = cardBackgroundColor
                        )
                    }
                }
            }
        }
    )

    // Custom Amount Dialog
    if (showCustomAmountDialog) {
        CustomAmountDialog(
            onDismiss = { showCustomAmountDialog = false },
            onSave = { amount ->
                viewModel.addWater(amount)
                showCustomAmountDialog = false
            },
            accentColor = accentColor
        )
    }
}

@Composable
fun HydrationProgressCard(
    currentAmount: Int,
    goalAmount: Int,
    progress: Float,
    cardBackgroundColor: Color,
    accentColor: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💧",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hidrasi Hari Ini",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Circular Progress Indicator
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 12.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2

                    // Background circle
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        radius = radius,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress arc
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF81C784),
                                Color(0xFF4FC3F7),
                                Color(0xFF0288D1)
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${currentAmount}ml",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = "dari ${goalAmount}ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = accentColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            val percentage = (progress * 100).toInt()
            Text(
                text = "$percentage% tercapai",
                style = MaterialTheme.typography.bodyMedium,
                color = if (percentage >= 100) Color(0xFF4CAF50) else Color.Gray
            )

            if (percentage >= 100) {
                Text(
                    text = "🎉 Target harian tercapai!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickAddButtonsCard(
    onAddWater: (Int) -> Unit,
    cardBackgroundColor: Color,
    buttonColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Tambah Cepat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickAddButton("💧 250ml", 250, onAddWater, buttonColor)
                QuickAddButton("🥤 500ml", 500, onAddWater, buttonColor)
                QuickAddButton("🍶 1L", 1000, onAddWater, buttonColor)
                QuickAddButton("🍯 1.5L", 1500, onAddWater, buttonColor)
            }
        }
    }
}

@Composable
fun QuickAddButton(
    text: String,
    amount: Int,
    onAddWater: (Int) -> Unit,
    buttonColor: Color
) {
    Button(
        onClick = { onAddWater(amount) },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun StatisticsCard(
    streak: Int,
    weeklyAverage: Int,
    cardBackgroundColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistik",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    icon = "🔥",
                    value = "$streak",
                    label = "Hari Streak",
                    accentColor = accentColor
                )
                StatisticItem(
                    icon = "📊",
                    value = "${weeklyAverage}ml",
                    label = "Rata-rata Mingguan",
                    accentColor = accentColor
                )
                StatisticItem(
                    icon = "🎯",
                    value = "85%",
                    label = "Target Tercapai",
                    accentColor = accentColor
                )
            }
        }
    }
}

@Composable
fun StatisticItem(
    icon: String,
    value: String,
    label: String,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TimeFilterCard(
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    cardBackgroundColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Periode",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Hari Ini", "Minggu Ini", "Bulan Ini")) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { onFilterChange(filter) },
                        label = { Text(filter) }
                    )
                }
            }
        }
    }
}

@Composable
fun HydrationHistoryCard(
    entries: List<HydrationEntity>,
    cardBackgroundColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Riwayat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada data",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                entries.take(10).forEach { entry ->
                    HydrationHistoryItem(entry)
                }
            }
        }
    }
}

@Composable
fun HydrationHistoryItem(entry: HydrationEntity) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF81C784)),
            contentAlignment = Alignment.Center
        ) {
            // Use emoji based on drink type if available, otherwise default to water
            val emoji = when (entry.drinkType?.lowercase()) {
                "tea" -> "🍵"
                "coffee" -> "☕"
                "juice" -> "🧃"
                "sports_drink" -> "🥤"
                "milk" -> "🥛"
                else -> "💧"
            }
            Text(
                text = emoji,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Display text with container size if available
            val displayText = if (entry.containerSize != null) {
                "${entry.amountMl} ml (${entry.containerSize}ml container)"
            } else {
                "${entry.amountMl} ml"
            }

            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            // Format timestamp
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            Text(
                text = sdf.format(Date(entry.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAmountDialog(
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit,
    accentColor: Color
) {
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Tambah Hidrasi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Jumlah (ml)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text("Batal")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            amount.toIntOrNull()?.let { amt ->
                                if (amt > 0) onSave(amt)
                            }
                        },
                        enabled = amount.toIntOrNull()?.let { it > 0 } ?: false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor
                        )
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

// Helper functions
fun calculateStreak(entries: List<HydrationEntity>, dailyGoal: Int): Int {
    // Implementation for calculating consecutive days meeting goal
    return 5 // Placeholder
}

fun calculateWeeklyAverage(entries: List<HydrationEntity>): Int {
    // Implementation for calculating weekly average
    return 1800 // Placeholder
}

fun getFilteredEntries(entries: List<HydrationEntity>, filter: String): List<HydrationEntity> {
    val calendar = Calendar.getInstance()
    return when (filter) {
        "Hari Ini" -> {
            val today = calendar.get(Calendar.DAY_OF_YEAR)
            entries.filter {
                val entryCalendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                entryCalendar.get(Calendar.DAY_OF_YEAR) == today
            }
        }
        "Minggu Ini" -> {
            val thisWeek = calendar.get(Calendar.WEEK_OF_YEAR)
            entries.filter {
                val entryCalendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                entryCalendar.get(Calendar.WEEK_OF_YEAR) == thisWeek
            }
        }
        "Bulan Ini" -> {
            val thisMonth = calendar.get(Calendar.MONTH)
            entries.filter {
                val entryCalendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                entryCalendar.get(Calendar.MONTH) == thisMonth
            }
        }
        else -> entries
    }
}

private fun Long.toDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}