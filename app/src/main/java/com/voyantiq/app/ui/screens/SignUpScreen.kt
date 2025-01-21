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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class SignUpFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val countryCode: String = "+1",
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val streetAddress: String = "",
    val streetAddressError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val state: String = "",
    val stateError: String? = null,
    val zipCode: String = "",
    val zipCodeError: String? = null,
    val termsAccepted: Boolean = false,
    val termsError: String? = null
)

@Composable
fun SignUpScreen(
    onSignUpComplete: () -> Unit,
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    var formState by remember { mutableStateOf(SignUpFormState()) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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
                "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            // Spacer for alignment
            Box(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(24.dp))

        // Name Fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = formState.firstName,
                onValueChange = {
                    formState = formState.copy(
                        firstName = it,
                        firstNameError = validateName(it, "First name")
                    )
                },
                label = { Text("First Name*") },
                isError = formState.firstNameError != null,
                supportingText = { formState.firstNameError?.let { Text(it) } },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = formState.lastName,
                onValueChange = {
                    formState = formState.copy(
                        lastName = it,
                        lastNameError = validateName(it, "Last name")
                    )
                },
                label = { Text("Last Name*") },
                isError = formState.lastNameError != null,
                supportingText = { formState.lastNameError?.let { Text(it) } },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(Modifier.height(16.dp))

        // Phone Number with Country Code
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Country Code Dropdown
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { },
                modifier = Modifier.width(96.dp)
            ) {
                OutlinedTextField(
                    value = formState.countryCode,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Code") },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, "dropdown") },
                    modifier = Modifier.menuAnchor(),
                    singleLine = true
                )
            }

            // Phone Number
            OutlinedTextField(
                value = formState.phoneNumber,
                onValueChange = {
                    if (it.length <= 10) {
                        formState = formState.copy(
                            phoneNumber = it.filter { char -> char.isDigit() },
                            phoneNumberError = validatePhone(it)
                        )
                    }
                },
                label = { Text("Phone Number*") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = formState.phoneNumberError != null,
                supportingText = { formState.phoneNumberError?.let { Text(it) } },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = formState.email,
            onValueChange = {
                formState = formState.copy(
                    email = it,
                    emailError = validateEmail(it)
                )
            },
            label = { Text("Email Address*") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = formState.emailError != null,
            supportingText = { formState.emailError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = formState.password,
            onValueChange = {
                formState = formState.copy(
                    password = it,
                    passwordError = validatePassword(it)
                )
            },
            label = { Text("Password*") },
            visualTransformation = if (formState.showPassword)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = {
                    formState = formState.copy(showPassword = !formState.showPassword)
                }) {
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
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Address Fields
        OutlinedTextField(
            value = formState.streetAddress,
            onValueChange = {
                formState = formState.copy(
                    streetAddress = it,
                    streetAddressError = validateStreetAddress(it)
                )
            },
            label = { Text("Street Address*") },
            isError = formState.streetAddressError != null,
            supportingText = { formState.streetAddressError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = formState.city,
                onValueChange = {
                    formState = formState.copy(
                        city = it,
                        cityError = validateCity(it)
                    )
                },
                label = { Text("City*") },
                isError = formState.cityError != null,
                supportingText = { formState.cityError?.let { Text(it) } },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = formState.state,
                onValueChange = {
                    formState = formState.copy(
                        state = it,
                        stateError = validateState(it)
                    )
                },
                label = { Text("State*") },
                isError = formState.stateError != null,
                supportingText = { formState.stateError?.let { Text(it) } },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.zipCode,
            onValueChange = {
                if (it.length <= 5) {
                    formState = formState.copy(
                        zipCode = it.filter { char -> char.isDigit() },
                        zipCodeError = validateZipCode(it)
                    )
                }
            },
            label = { Text("ZIP Code*") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = formState.zipCodeError != null,
            supportingText = { formState.zipCodeError?.let { Text(it) } },
            modifier = Modifier.width(200.dp),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Terms and Privacy Policy
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formState.termsAccepted,
                onCheckedChange = {
                    formState = formState.copy(
                        termsAccepted = it,
                        termsError = if (it) null else "You must accept the terms"
                    )
                }
            )
            Text(
                "I agree to the ",
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(onClick = { showTermsDialog = true }) {
                Text("Terms")
            }
            Text(" and ")
            TextButton(onClick = { showPrivacyDialog = true }) {
                Text("Privacy Policy")
            }
        }

        if (formState.termsError != null) {
            Text(
                formState.termsError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (validateForm(formState)) {
                    isLoading = true
                    // Handle sign up
                    onSignUpComplete()
                } else {
                    showErrorDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && formState.termsAccepted
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Create Account")
            }
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text("Please fix the errors in the form") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Terms Dialog
    if (showTermsDialog) {
        Dialog(onDismissRequest = { showTermsDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Terms of Service",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Terms content goes here...")
                    Spacer(Modifier.height(16.dp))
                    TextButton(
                        onClick = { showTermsDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        Dialog(onDismissRequest = { showPrivacyDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Privacy Policy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Privacy policy content goes here...")
                    Spacer(Modifier.height(16.dp))
                    TextButton(
                        onClick = { showPrivacyDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

// Validation Functions
private fun validateName(name: String, fieldName: String): String? {
    return when {
        name.isBlank() -> "$fieldName is required"
        name.length < 2 -> "$fieldName must be at least 2 characters"
        !name.matches(Regex("^[a-zA-Z ]+$")) -> "$fieldName can only contain letters"
        else -> null
    }
}

private fun validatePhone(phone: String): String? {
    return when {
        phone.isBlank() -> "Phone number is required"
        phone.length != 10 -> "Phone number must be 10 digits"
        !phone.all { it.isDigit() } -> "Phone number can only contain digits"
        else -> null
    }
}

private fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email is required"
        !email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")) -> "Invalid email format"
        else -> null
    }
}

private fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        !password.matches(Regex(".*[A-Z].*")) -> "Password must contain at least one uppercase letter"
        !password.matches(Regex(".*[a-z].*")) -> "Password must contain at least one lowercase letter"
        !password.matches(Regex(".*[0-9].*")) -> "Password must contain at least one number"
        !password.matches(Regex(".*[!@#\$%^&*].*")) -> "Password must contain at least one special character"
        else -> null
    }
}

private fun validateStreetAddress(address: String): String? {
    return when {
        address.isBlank() -> "Street address is required"
        address.length < 5 -> "Please enter a valid street address"
        else -> null
    }
}

private fun validateCity(city: String): String? {
    return when {
        city.isBlank() -> "City is required"
        !city.matches(Regex("^[a-zA-Z ]+$")) -> "City can only contain letters"
        else -> null
    }
}

private fun validateState(state: String): String? {
    return when {
        state.isBlank() -> "State is required"
        !state.matches(Regex("^[a-zA-Z ]+$")) -> "State can only contain letters"
        else -> null
    }
}

private fun validateZipCode(zipCode: String): String? {
    return when {
        zipCode.isBlank() -> "ZIP code is required"
        zipCode.length != 5 -> "ZIP code must be 5 digits"
        !zipCode.all { it.isDigit() } -> "ZIP code can only contain numbers"
        else -> null
    }
}

private fun validateForm(state: SignUpFormState): Boolean {
    return validateName(state.firstName, "First name") == null &&
            validateName(state.lastName, "Last name") == null &&
            validatePhone(state.phoneNumber) == null &&
            validateEmail(state.email) == null &&
            validatePassword(state.password) == null &&
            validateStreetAddress(state.streetAddress) == null &&
            validateCity(state.city) == null &&
            validateState(state.state) == null &&
            validateZipCode(state.zipCode) == null &&
            state.termsAccepted
}