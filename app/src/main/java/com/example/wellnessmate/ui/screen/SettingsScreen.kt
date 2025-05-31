package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wellnessmate.Screen
import com.example.wellnessmate.utils.AuthHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF4FC3F7),
    buttonColor: Color = Color(0xFF0288D1)
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var dailyWaterGoal by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var reminderInterval by remember { mutableStateOf("30") }
    var showClearDataDialog by remember { mutableStateOf(false) }

    // Validation states
    var nameError by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    var waterGoalError by remember { mutableStateOf(false) }

    // Validation functions
    fun validateInputs(): Boolean {
        nameError = name.isBlank()
        ageError = age.toIntOrNull()?.let { it < 1 || it > 120 } ?: true
        weightError = weight.toFloatOrNull()?.let { it < 30 || it > 300 } ?: true
        heightError = height.toIntOrNull()?.let { it < 100 || it > 250 } ?: true
        waterGoalError = dailyWaterGoal.toIntOrNull()?.let { it < 500 || it > 5000 } ?: true

        return !(nameError || ageError || weightError || heightError || waterGoalError)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Pengaturan & Profil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Profile Settings Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = buttonColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Informasi Profil",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = buttonColor
                        )
                    }

                    // Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                        },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError,
                        supportingText = if (nameError) {
                            { Text("Nama tidak boleh kosong") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = buttonColor,
                            focusedLabelColor = buttonColor
                        )
                    )

                    // Age field
                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                age = it
                                ageError = false
                            }
                        },
                        label = { Text("Usia (tahun)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = ageError,
                        supportingText = if (ageError) {
                            { Text("Masukkan usia yang valid (1-120 tahun)") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = buttonColor,
                            focusedLabelColor = buttonColor
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Weight field
                        OutlinedTextField(
                            value = weight,
                            onValueChange = {
                                if (it.isEmpty() || (it.toFloatOrNull() != null && it.length <= 5)) {
                                    weight = it
                                    weightError = false
                                }
                            },
                            label = { Text("Berat (kg)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = weightError,
                            supportingText = if (weightError) {
                                { Text("30-300 kg") }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = buttonColor,
                                focusedLabelColor = buttonColor
                            )
                        )

                        // Height field
                        OutlinedTextField(
                            value = height,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                    height = it
                                    heightError = false
                                }
                            },
                            label = { Text("Tinggi (cm)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = heightError,
                            supportingText = if (heightError) {
                                { Text("100-250 cm") }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = buttonColor,
                                focusedLabelColor = buttonColor
                            )
                        )
                    }

                    // Daily water goal
                    OutlinedTextField(
                        value = dailyWaterGoal,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 4) {
                                dailyWaterGoal = it
                                waterGoalError = false
                            }
                        },
                        label = { Text("Target Air Harian (ml)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = waterGoalError,
                        supportingText = if (waterGoalError) {
                            { Text("Masukkan target yang valid (500-5000 ml)") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = buttonColor,
                            focusedLabelColor = buttonColor
                        )
                    )
                }
            }

            // App Settings Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = buttonColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Pengaturan Aplikasi",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = buttonColor
                        )
                    }

                    // Notifications toggle
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Aktifkan Notifikasi",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Dapatkan pengingat untuk minum air",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = buttonColor,
                                    checkedTrackColor = buttonColor.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }

                    // Reminder interval (only show if notifications enabled)
                    if (notificationsEnabled) {
                        OutlinedTextField(
                            value = reminderInterval,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                    reminderInterval = it
                                }
                            },
                            label = { Text("Interval Pengingat (menit)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            supportingText = { Text("Seberapa sering mengingatkan Anda (15-180 menit)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = buttonColor,
                                focusedLabelColor = buttonColor
                            )
                        )
                    }
                }
            }

            // Action Buttons
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = buttonColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Tindakan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = buttonColor
                        )
                    }

                    // Save button
                    Button(
                        onClick = {
                            if (validateInputs()) {
                                // Handle save profile logic
                                // You can call your repository methods here
                                // Show success snackbar
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SIMPAN PROFIL", fontWeight = FontWeight.Bold)
                    }

                    // Clear data button
                    OutlinedButton(
                        onClick = {
                            showClearDataDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("HAPUS SEMUA DATA", fontWeight = FontWeight.Bold)
                    }

                    // Logout button
                    OutlinedButton(
                        onClick = {
                            AuthHelper.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) // Clear backstack so user can't return with back button
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF0288D1)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("KELUAR", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Clear data confirmation dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = {
                Text(
                    "Hapus Semua Data",
                    fontWeight = FontWeight.Bold,
                    color = buttonColor
                )
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus semua data? Tindakan ini tidak dapat dibatalkan.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Handle clear data logic
                        showClearDataDialog = false
                        // Clear all fields
                        name = ""
                        age = ""
                        weight = ""
                        height = ""
                        dailyWaterGoal = ""
                        notificationsEnabled = true
                        reminderInterval = "30"
                    }
                ) {
                    Text("HAPUS", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("BATAL", color = buttonColor, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }
}