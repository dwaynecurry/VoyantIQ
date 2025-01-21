package com.voyantiq.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// State class for login form
data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false
)

// UI state for login
sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    object Success : LoginUiState()
}

@Composable
fun LoginScreen(
    onLoginComplete: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var formState by remember { mutableStateOf(LoginFormState()) }
    var uiState by remember { mutableStateOf<LoginUiState>(LoginUiState.Initial) }
    val scope = rememberCoroutineScope()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Loading Dialog
    if (uiState is LoginUiState.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Signing In") },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text("Please wait...")
                }
            },
            confirmButton = { }
        )
    }

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
                enabled = uiState !is LoginUiState.Loading
            ) {
                Text("←")
            }
            Text(
                text = "Sign In",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072BC)
            )
            if (uiState is LoginUiState.Error) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            } else {
                Box(modifier = Modifier.size(48.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = formState.email,
            onValueChange = {
                formState = formState.copy(
                    email = it,
                    emailError = validateEmail(it)
                )
            },
            label = { Text("Email Address") },
            placeholder = { Text("Enter your email", color = Color.Gray.copy(alpha = 0.5f)) },
            isError = formState.emailError != null,
            supportingText = { formState.emailError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = formState.password,
            onValueChange = {
                formState = formState.copy(
                    password = it,
                    passwordError = validatePassword(it)
                )
            },
            label = { Text("Password") },
            placeholder = { Text("Enter your password", color = Color.Gray.copy(alpha = 0.5f)) },
            visualTransformation = if (formState.showPassword)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(
                    onClick = {
                        formState = formState.copy(showPassword = !formState.showPassword)
                    }
                ) {
                    Icon(
                        imageVector = if (formState.showPassword)
                            Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (formState.showPassword)
                            "Hide password" else "Show password"
                    )
                }
            },
            isError = formState.passwordError != null,
            supportingText = { formState.passwordError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password Link
        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.End),
            enabled = uiState !is LoginUiState.Loading
        ) {
            Text(
                "Forgot Password?",
                color = Color(0xFF0072BC)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                scope.launch {
                    try {
                        uiState = LoginUiState.Loading
                        // Simulate network call
                        delay(2000)
                        if (validateForm()) {
                            uiState = LoginUiState.Success
                            onLoginComplete()
                        } else {
                            throw Exception("Invalid email or password")
                        }
                    } catch (e: Exception) {
                        uiState = LoginUiState.Error(e.message ?: "An unknown error occurred")
                        errorMessage = e.message ?: "An unknown error occurred"
                        showErrorDialog = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0072BC)
            ),
            enabled = isFormValid() && uiState !is LoginUiState.Loading
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Sign In")
            }
        }

        // Error message
        AnimatedVisibility(visible = uiState is LoginUiState.Error) {
            Text(
                text = (uiState as? LoginUiState.Error)?.message ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

private fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email is required"
        !email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")) -> "Invalid email format"
        else -> null
    }
}

private fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        else -> null
    }
}

private fun validateForm(): Boolean {
    // Add comprehensive form validation
    return true
}

private fun isFormValid(): Boolean {
    // Add comprehensive form validation
    return true
}