package com.voyantiq.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voyantiq.app.ui.components.Address
import com.voyantiq.app.ui.components.AddressFields
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// State class to handle form validation
data class SignUpFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val address: Address = Address("", "", "", "", ""),
    val addressError: String? = null,
    val termsAccepted: Boolean = false,
    val showPassword: Boolean = false
)

// Sealed class for UI state
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
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    var formState by remember { mutableStateOf(SignUpFormState()) }
    var uiState by remember { mutableStateOf<SignUpUiState>(SignUpUiState.Initial) }
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Bar with back button and error indicator
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
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        showErrorDialog = true
                    }
                )
            } else {
                Box(modifier = Modifier.size(48.dp))
            }
        }

        // Form Fields with error states
        OutlinedTextField(
            value = formState.firstName,
            onValueChange = {
                formState = formState.copy(
                    firstName = it,
                    firstNameError = validateFirstName(it)
                )
            },
            label = { Text("First Name") },
            placeholder = { Text("Enter your first name", color = Color.Gray.copy(alpha = 0.5f)) },
            isError = formState.firstNameError != null,
            supportingText = { formState.firstNameError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is SignUpUiState.Loading,
            singleLine = true
        )

        // Similar updates for other fields...

        // Sign Up Button with loading state
        Button(
            onClick = {
                scope.launch {
                    try {
                        uiState = SignUpUiState.Loading
                        // Simulate network call
                        delay(2000)
                        if (validateForm()) {
                            uiState = SignUpUiState.Success
                            onSignUpComplete()
                        } else {
                            throw Exception("Please fix the errors in the form")
                        }
                    } catch (e: Exception) {
                        uiState = SignUpUiState.Error(e.message ?: "An unknown error occurred")
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
            enabled = isFormValid() && uiState !is SignUpUiState.Loading
        ) {
            if (uiState is SignUpUiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Sign Up")
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

private fun validateFirstName(firstName: String): String? {
    return when {
        firstName.isBlank() -> "First name is required"
        firstName.length < 2 -> "First name must be at least 2 characters"
        !firstName.matches(Regex("^[a-zA-Z ]+$")) -> "First name can only contain letters"
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