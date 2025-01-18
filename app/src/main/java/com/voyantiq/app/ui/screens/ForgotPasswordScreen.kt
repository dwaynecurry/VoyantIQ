package com.voyantiq.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class ResetPasswordUiState {
    object Initial : ResetPasswordUiState()
    object Loading : ResetPasswordUiState()
    object EmailSent : ResetPasswordUiState()
    data class Error(val message: String) : ResetPasswordUiState()
}

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onResetSent: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var uiState by remember { mutableStateOf<ResetPasswordUiState>(ResetPasswordUiState.Initial) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                enabled = uiState !is ResetPasswordUiState.Loading
            ) {
                Text("←")
            }
            Text(
                text = "Reset Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072BC)
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Instructions
        Text(
            text = "Enter your email address and we'll send you instructions to reset your password.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            placeholder = { Text("Enter your email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is ResetPasswordUiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send Reset Link Button
        Button(
            onClick = {
                scope.launch {
                    uiState = ResetPasswordUiState.Loading
                    delay(1500) // Simulate network request
                    if (email.contains("@")) {
                        uiState = ResetPasswordUiState.EmailSent
                        onResetSent()
                    } else {
                        uiState = ResetPasswordUiState.Error("Invalid email address")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = email.isNotBlank() && uiState !is ResetPasswordUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0072BC)
            )
        ) {
            if (uiState is ResetPasswordUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Send Reset Link")
            }
        }

        // Success Message
        AnimatedVisibility(
            visible = uiState is ResetPasswordUiState.EmailSent,
            enter = fadeIn() + slideInVertically()
        ) {
            Text(
                text = "Reset link sent! Check your email.",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Error Message
        AnimatedVisibility(
            visible = uiState is ResetPasswordUiState.Error,
            enter = fadeIn() + slideInVertically()
        ) {
            Text(
                text = (uiState as? ResetPasswordUiState.Error)?.message ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}