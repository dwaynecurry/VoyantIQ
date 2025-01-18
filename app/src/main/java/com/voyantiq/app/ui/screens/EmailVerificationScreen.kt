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

sealed class VerificationUiState {
    object Initial : VerificationUiState()
    object Loading : VerificationUiState()
    object CodeSent : VerificationUiState()
    object Verified : VerificationUiState()
    data class Error(val message: String) : VerificationUiState()
}

@Composable
fun EmailVerificationScreen(
    email: String,
    onVerificationComplete: () -> Unit,
    onBackClick: () -> Unit,
    onResendCode: () -> Unit
) {
    var verificationCode by remember { mutableStateOf("") }
    var uiState by remember { mutableStateOf<VerificationUiState>(VerificationUiState.Initial) }
    var remainingTime by remember { mutableStateOf(60) }
    val scope = rememberCoroutineScope()

    // Countdown timer for resend code
    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                enabled = uiState !is VerificationUiState.Loading
            ) {
                Text("←")
            }
            Text(
                text = "Verify Email",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072BC)
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Instructions
        Text(
            text = "We've sent a verification code to:",
            textAlign = TextAlign.Center
        )

        Text(
            text = email,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0072BC),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Verification Code Input
        OutlinedTextField(
            value = verificationCode,
            onValueChange = {
                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                    verificationCode = it
                }
            },
            label = { Text("Verification Code") },
            placeholder = { Text("Enter 6-digit code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Verify Button
        Button(
            onClick = {
                scope.launch {
                    uiState = VerificationUiState.Loading
                    delay(1500) // Simulate verification
                    if (verificationCode.length == 6) {
                        uiState = VerificationUiState.Verified
                        onVerificationComplete()
                    } else {
                        uiState = VerificationUiState.Error("Invalid verification code")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = verificationCode.length == 6 && uiState !is VerificationUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0072BC)
            )
        ) {
            if (uiState is VerificationUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Verify")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend Code
        if (remainingTime > 0) {
            Text(
                text = "Resend code in ${remainingTime}s",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            TextButton(
                onClick = {
                    scope.launch {
                        remainingTime = 60
                        onResendCode()
                    }
                }
            ) {
                Text("Resend Code")
            }
        }
    }
}