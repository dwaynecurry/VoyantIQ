package com.voyantiq.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false
)

@Composable
fun LoginScreen(
    onLoginComplete: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit  // Added this parameter
) {
    var formState by remember { mutableStateOf(LoginFormState()) }
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
            Text(
                "Sign In",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            // Spacer for alignment
            Box(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(48.dp))

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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = formState.emailError != null,
            supportingText = { formState.emailError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = formState.password,
            onValueChange = {
                formState = formState.copy(
                    password = it,
                    passwordError = validateLoginPassword(it)
                )
            },
            label = { Text("Password") },
            visualTransformation = if (formState.showPassword)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        formState = formState.copy(showPassword = !formState.showPassword)
                    },
                    enabled = !isLoading
                ) {
                    Icon(
                        if (formState.showPassword) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        "toggle password visibility"
                    )
                }
            },
            isError = formState.passwordError != null,
            supportingText = { formState.passwordError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(Modifier.height(8.dp))

        // Forgot Password Link
        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading
        ) {
            Text(
                "Forgot Password?",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                if (validateLoginForm(formState)) {
                    isLoading = true
                    // Here you'll add your login logic later
                    onLoginComplete()
                } else {
                    showErrorDialog = true
                    errorMessage = "Please check your email and password"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && formState.email.isNotBlank() && formState.password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Sign In")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Sign Up Link (Added this section)
        TextButton(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Don't have an account? Sign Up")
        }

        // Error message
        AnimatedVisibility(
            visible = formState.emailError != null || formState.passwordError != null
        ) {
            Text(
                text = formState.emailError ?: formState.passwordError ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

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
}

private fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email is required"
        !email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")) -> "Invalid email format"
        else -> null
    }
}

private fun validateLoginPassword(password: String): String? {
    return when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        else -> null
    }
}

private fun validateLoginForm(state: LoginFormState): Boolean {
    return validateEmail(state.email) == null &&
            validateLoginPassword(state.password) == null
}