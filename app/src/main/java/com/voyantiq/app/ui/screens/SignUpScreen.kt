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
import com.voyantiq.app.data.auth.AuthState
import com.voyantiq.app.ui.viewmodel.AuthViewModel

data class SignUpFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false
)

sealed class SignUpUiState {
    object Initial : SignUpUiState()
    object Loading : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
    object Success : SignUpUiState()
}

@Composable
fun SignUpScreen(
    onSignUpComplete: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var formState by remember { mutableStateOf(SignUpFormState()) }
    var uiState by remember { mutableStateOf<SignUpUiState>(SignUpUiState.Initial) }
    val authState by viewModel.authState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                uiState = SignUpUiState.Success
                onSignUpComplete()
            }
            is AuthState.Error -> {
                uiState = SignUpUiState.Error((authState as AuthState.Error).message)
                errorMessage = (authState as AuthState.Error).message
                showErrorDialog = true
            }
            is AuthState.Loading -> {
                uiState = SignUpUiState.Loading
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
    if (uiState is SignUpUiState.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Creating Account") },
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
                enabled = uiState !is SignUpUiState.Loading
            ) {
                Text("←")
            }
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072BC)
            )
            if (uiState is SignUpUiState.Error) {
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
            enabled = uiState !is SignUpUiState.Loading,
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
            enabled = uiState !is SignUpUiState.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = formState.confirmPassword,
            onValueChange = {
                formState = formState.copy(
                    confirmPassword = it,
                    confirmPasswordError = validateConfirmPassword(formState.password, it)
                )
            },
            label = { Text("Confirm Password") },
            placeholder = { Text("Confirm your password", color = Color.Gray.copy(alpha = 0.5f)) },
            visualTransformation = if (formState.showConfirmPassword)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(
                    onClick = {
                        formState = formState.copy(showConfirmPassword = !formState.showConfirmPassword)
                    }
                ) {
                    Icon(
                        imageVector = if (formState.showConfirmPassword)
                            Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (formState.showConfirmPassword)
                            "Hide password" else "Show password"
                    )
                }
            },
            isError = formState.confirmPasswordError != null,
            supportingText = { formState.confirmPasswordError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is SignUpUiState.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (validateForm(formState)) {
                    viewModel.signUp(formState.email, formState.password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0072BC)
            ),
            enabled = isFormValid(formState) && uiState !is SignUpUiState.Loading
        ) {
            if (uiState is SignUpUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Create Account")
            }
        }

        // Error message
        AnimatedVisibility(visible = uiState is SignUpUiState.Error) {
            Text(
                text = (uiState as? SignUpUiState.Error)?.message ?: "",
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
        !password.matches(Regex(".*[A-Z].*")) -> "Password must contain at least one uppercase letter"
        !password.matches(Regex(".*[a-z].*")) -> "Password must contain at least one lowercase letter"
        !password.matches(Regex(".*\\d.*")) -> "Password must contain at least one number"
        else -> null
    }
}

private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
    return when {
        confirmPassword.isBlank() -> "Please confirm your password"
        confirmPassword != password -> "Passwords do not match"
        else -> null
    }
}

private fun validateForm(formState: SignUpFormState): Boolean {
    return formState.emailError == null &&
            formState.passwordError == null &&
            formState.confirmPasswordError == null &&
            formState.email.isNotBlank() &&
            formState.password.isNotBlank() &&
            formState.confirmPassword.isNotBlank()
}

private fun isFormValid(formState: SignUpFormState): Boolean {
    return validateForm(formState)
}