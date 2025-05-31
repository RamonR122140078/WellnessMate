package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wellnessmate.R
import com.example.wellnessmate.data.entity.MoodEntryWithActivity
import com.example.wellnessmate.ui.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MoodJournalScreen(
    navController: NavHostController,
    viewModel: MoodViewModel = viewModel(),
    backgroundColor: Color = Color(0xFF4FC3F7),
    accentColor: Color = Color(0xFF0288D1)
) {
    val mainBackgroundColor = backgroundColor
    val cardBackgroundColor = Color.White
    val buttonColor = accentColor

    var showAddMoodDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedActivityFilter by remember { mutableStateOf<String?>(null) }

    // Get mood entries from viewModel
    val moods by viewModel.allMoods.collectAsState(initial = emptyList())

    // Activity options for selection
    val activityOptions = listOf(
        "Olahraga", "Kerja", "Istirahat", "Makan", "Keluarga",
        "Teman", "Hobi", "Belajar", "Medsos", "Lainnya"
    )

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
                            text = "Jurnal Mood",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor
                ),
                actions = {
                    Box {
                        // Removed FilterAlt icon and using Text instead
                        Text(
                            text = "Filter",
                            color = Color.White,
                            modifier = Modifier
                                .clickable { showFilterMenu = true }
                                .padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Semua") },
                                onClick = {
                                    selectedFilter = null
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Senang 😊") },
                                onClick = {
                                    selectedFilter = "😊"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Biasa 😐") },
                                onClick = {
                                    selectedFilter = "😐"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sedih 😢") },
                                onClick = {
                                    selectedFilter = "😢"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Marah 😠") },
                                onClick = {
                                    selectedFilter = "😠"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mengantuk 😴") },
                                onClick = {
                                    selectedFilter = "😴"
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMoodDialog = true },
                containerColor = buttonColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Mood")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Current mood status
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = cardBackgroundColor
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Removed CalendarMonth icon and using Text instead
                                Text(
                                    text = "📅",
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val today = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                                    .format(Date())
                                Text(
                                    text = today,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Bagaimana perasaan Anda hari ini?",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { showAddMoodDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = buttonColor
                                ),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Catat Mood")
                            }
                        }
                    }

                    // Filter chips for activities
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                                text = "Aktivitas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                activityOptions.forEach { activity ->
                                    FilterChip(
                                        selected = selectedActivityFilter == activity,
                                        onClick = {
                                            selectedActivityFilter = if (selectedActivityFilter == activity) null else activity
                                        },
                                        label = { Text(activity) },
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Mood history
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Riwayat Mood",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                if (selectedFilter != null) {
                                    AssistChip(
                                        onClick = { selectedFilter = null },
                                        label = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Filter: $selectedFilter")
                                                Text(" ✕", fontSize = 14.sp)
                                            }
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = accentColor.copy(alpha = 0.2f)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val filteredMoods = moods.filter { moodEntry ->
                                val moodMatch = selectedFilter?.let { moodEntry.moodEmoji == it } ?: true
                                val activityMatch = selectedActivityFilter?.let { moodEntry.activity == it } ?: true
                                moodMatch && activityMatch
                            }

                            if (filteredMoods.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ada data mood",
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                LazyColumn {
                                    items(filteredMoods) { moodItem ->
                                        MoodItemCard(moodItem)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    // Dialog untuk menambahkan mood baru
    if (showAddMoodDialog) {
        AddMoodDialog(
            onDismiss = { showAddMoodDialog = false },
            onSave = { emoji, note, activity ->
                viewModel.addMood(emoji, note, activity)
                showAddMoodDialog = false
            },
            accentColor = accentColor,
            activityOptions = activityOptions
        )
    }
}

@Composable
fun MoodItemCard(moodItem: MoodEntryWithActivity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getMoodColor(moodItem.moodEmoji)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = moodItem.moodEmoji,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moodItem.note ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (moodItem.activity.isNotEmpty()) {
                        AssistChip(
                            onClick = { },
                            enabled = false,
                            label = { Text(moodItem.activity, fontSize = 12.sp) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    val date = Date(moodItem.timestamp)
                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val formattedDate = formatter.format(date)
                    Text(
                        formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodDialog(
    onDismiss: () -> Unit,
    onSave: (emoji: String, note: String, activity: String) -> Unit,
    accentColor: Color,
    activityOptions: List<String>
) {
    var selectedEmoji by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedActivity by remember { mutableStateOf("") }

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
                    text = "Bagaimana perasaan Anda?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pilih mood:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("😊", "😐", "😢", "😠", "😴").forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedEmoji == emoji)
                                        getMoodColor(emoji)
                                    else
                                        Color.LightGray.copy(alpha = 0.3f)
                                )
                                .border(
                                    width = if (selectedEmoji == emoji) 2.dp else 0.dp,
                                    color = accentColor,
                                    shape = CircleShape
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Aktivitas terkait:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(activityOptions) { activity ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedActivity = activity }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selectedActivity == activity)
                                            accentColor
                                        else
                                            Color.LightGray
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedActivity == activity) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = activity,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ceritakan lebih detail...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
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
                            if (selectedEmoji.isNotEmpty()) {
                                onSave(selectedEmoji, note, selectedActivity)
                            }
                        },
                        enabled = selectedEmoji.isNotEmpty(),
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

fun getMoodColor(emoji: String): Color {
    return when (emoji) {
        "😊" -> Color(0xFFAAD16E) // Hijau untuk senang
        "😐" -> Color(0xFFFFD867) // Kuning untuk biasa
        "😢" -> Color(0xFF86B5FC) // Biru untuk sedih
        "😠" -> Color(0xFFFF7676) // Merah untuk marah
        "😴" -> Color(0xFFBFA8FC) // Ungu untuk mengantuk
        else -> Color.LightGray
    }
}