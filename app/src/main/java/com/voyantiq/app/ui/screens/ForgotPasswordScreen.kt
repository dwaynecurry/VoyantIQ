package com.voyantiq.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.voyantiq.app.ui.theme.VoyantColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.voyantiq.app.utils.validateEmail


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
    var emailError by remember { mutableStateOf<String?>(null) }
    var uiState by remember { mutableStateOf<ResetPasswordUiState>(ResetPasswordUiState.Initial) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
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
                Text(
                    "←",
                    color = VoyantColors.Primary
                )
            }
            Text(
                text = "Reset Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VoyantColors.Primary
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Instructions
        Text(
            text = "Enter your email address and we'll send you instructions to reset your password.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
            color = VoyantColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = validateEmail(it)
            },
            label = { Text("Email Address") },
            placeholder = {
                Text(
                    "Enter your email",
                    color = VoyantColors.PlaceholderText
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(
                        it,
                        color = VoyantColors.Error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is ResetPasswordUiState.Loading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VoyantColors.Primary,
                unfocusedBorderColor = VoyantColors.Divider,
                disabledBorderColor = VoyantColors.Disabled,
                focusedLabelColor = VoyantColors.Primary,
                unfocusedLabelColor = VoyantColors.TextSecondary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send Reset Link Button
        Button(
            onClick = {
                scope.launch {
                    emailError = validateEmail(email)
                    if (emailError == null) {
                        uiState = ResetPasswordUiState.Loading
                        try {
                            delay(1500) // Simulate network request
                            uiState = ResetPasswordUiState.EmailSent
                            showConfirmationDialog = true
                        } catch (e: Exception) {
                            uiState = ResetPasswordUiState.Error(
                                e.message ?: "An error occurred. Please try again."
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = email.isNotBlank() &&
                    emailError == null &&
                    uiState !is ResetPasswordUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = VoyantColors.Primary,
                disabledContainerColor = VoyantColors.Disabled
            )
        ) {
            if (uiState is ResetPasswordUiState.Loading) {
                CircularProgressIndicator(
                    color = VoyantColors.Background,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Send Reset Link",
                    color = VoyantColors.Background
                )
            }
        }

        // Error Message
        AnimatedVisibility(
            visible = uiState is ResetPasswordUiState.Error,
            enter = fadeIn() + slideInVertically()
        ) {
            Text(
                text = (uiState as? ResetPasswordUiState.Error)?.message ?: "",
                color = VoyantColors.Error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = VoyantColors.Background,
            titleContentColor = VoyantColors.Primary,
            textContentColor = VoyantColors.TextPrimary,
            title = { Text("Check Your Email") },
            text = {
                Text(
                    "We've sent password reset instructions to $email. " +
                            "Please check your email and follow the instructions to reset your password.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        onResetSent()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VoyantColors.Primary
                    )
                ) {
                    Text(
                        "OK",
                        color = VoyantColors.Background
                    )
                }
            }
        )
    }
}