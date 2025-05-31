package com.example.wellnessmate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wellnessmate.R
import com.example.wellnessmate.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
    backgroundColor: Color = Color(0xFF4FC3F7),
    buttonColor: Color = Color(0xFF0288D1)
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    // Input validation states
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // Email validation function
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Form validation
    fun validateForm(): Boolean {
        emailError = email.isBlank() || !isValidEmail(email)
        passwordError = password.length < 6
        return !emailError && !passwordError
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "WellnessMate Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    "Selamat Datang",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = buttonColor
                )

                Text(
                    "Masuk ke akun Anda",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = emailError,
                    supportingText = if (emailError) {
                        { Text("Masukkan email yang valid") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isError = passwordError,
                    supportingText = if (passwordError) {
                        { Text("Password minimal 6 karakter") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Remember me and Forgot Password
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text("Ingat Saya")
                    }

                    Text(
                        text = "Lupa Password?",
                        color = buttonColor,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                // Navigate to forgot password screen
                                // navController.navigate(Screen.ForgotPassword.route)
                            },
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = {
                        if (validateForm()) {
                            // Handle login logic here
                            // For now, navigate to dashboard
                            navController.navigate(Screen.Dashboard.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("MASUK", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider with "or" text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                    Text(
                        text = " ATAU ",
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                TextButton(
                    onClick = { navController.navigate(Screen.Register.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Belum punya akun? Daftar sekarang",
                        textAlign = TextAlign.Center,
                        color = buttonColor
                    )
                }
            }
        }
    }
}