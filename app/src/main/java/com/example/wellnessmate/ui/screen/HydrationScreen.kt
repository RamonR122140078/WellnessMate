package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wellnessmate.ui.viewmodel.HydrationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HydrationScreen(viewModel: HydrationViewModel = viewModel()) {
    val entries by viewModel.allEntries.collectAsState(initial = emptyList())
    val today by viewModel.totalToday.collectAsState(initial = 0)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Hidrasi Hari Ini", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = "$today ml",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addWater(250)
        }) {
            Text("Tambah 250 ml")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = entries) { entry ->
                Text("${entry.amountMl} ml - ${entry.timestamp.toDate()}")
            }
        }
    }
}

private fun Long.toDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}