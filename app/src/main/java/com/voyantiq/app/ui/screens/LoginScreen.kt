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
import androidx.hilt.navigation.compose.hiltViewModel
import com.voyantiq.app.BuildConfig
import com.voyantiq.app.data.auth.AuthState
import com.voyantiq.app.ui.viewmodel.AuthViewModel

data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false
)

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
    onForgotPasswordClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var formState by remember { mutableStateOf(LoginFormState()) }
    var uiState by remember { mutableStateOf<LoginUiState>(LoginUiState.Initial) }
    val authState by viewModel.authState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                uiState = LoginUiState.Success
                onLoginComplete()
            }
            is AuthState.Error -> {
                uiState = LoginUiState.Error((authState as AuthState.Error).message)
                errorMessage = (authState as AuthState.Error).message
                showErrorDialog = true
            }
            is AuthState.Loading -> {
                uiState = LoginUiState.Loading
            }
            else -> {}
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
            visualTransformation = if (formState.showPasswor<span class="ml-2" /><span class="inline-block w-3 h-3 rounded-full bg-neutral-a12 align-middle mb-[0.1rem]" />