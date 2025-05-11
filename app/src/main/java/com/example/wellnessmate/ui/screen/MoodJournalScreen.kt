package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wellnessmate.ui.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoodJournalScreen(viewModel: MoodViewModel = viewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Catat Mood Hari Ini", style = MaterialTheme.typography.displayMedium)

        var selectedEmoji by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }

        Row {
            listOf("😊", "😐", "😢", "😠", "😴").forEach { emoji ->
                TextButton(onClick = { selectedEmoji = emoji }) {
                    Text(text = emoji)
                }
            }
        }

        TextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Catatan") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (selectedEmoji.isNotEmpty()) {
                viewModel.addMood(selectedEmoji, note)
                note = ""
                selectedEmoji = ""
            }
        }) {
            Text("Simpan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Riwayat Mood:")
        val moods by viewModel.allMoods.collectAsState(initial = emptyList())
        moods.forEach {
            val date = Date(it.timestamp)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDate = formatter.format(date)
            Text("${it.moodEmoji} - ${it.note ?: ""} • $formattedDate")
        }
    }
}