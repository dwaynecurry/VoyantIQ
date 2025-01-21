package com.voyantiq.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voyantiq.app.ui.theme.VoyantColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditPreferencesClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            ProfileHeader()

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Profile Options
            ProfileSection(
                title = "Account Settings",
                options = listOf(
                    ProfileOption(
                        icon = Icons.Default.Person,
                        title = "Travel Preferences",
                        subtitle = "Update your travel preferences",
                        onClick = onEditPreferencesClick
                    ),
                    ProfileOption(
                        icon = Icons.Default.CreditCard,
                        title = "Payment Methods",
                        subtitle = "Manage your payment options",
                        onClick = onPaymentMethodsClick
                    ),
                    ProfileOption(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        subtitle = "App settings and notifications",
                        onClick = onSettingsClick
                    )
                )
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            ProfileSection(
                title = "Support",
                options = listOf(
                    ProfileOption(
                        icon = Icons.Default.Help,
                        title = "Help Center",
                        subtitle = "Get help with your bookings",
                        onClick = { /* TODO: Implement help center */ }
                    ),
                    ProfileOption(
                        icon = Icons.Default.Info,
                        title = "About",
                        subtitle = "App version and information",
                        onClick = { /* TODO: Implement about screen */ }
                    )
                )
            )

            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VoyantColors.Error
                )
            ) {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text("Log Out", color = VoyantColors.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Surface(
            modifier = Modifier.size(100.dp),
            shape = MaterialTheme.shapes.medium,
            color = VoyantColors.Primary.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .padding(24.dp)
                    .size(48.dp),
                tint = VoyantColors.Primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(
            "Dwayne Curry",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            "dwayne@example.com",
            style = MaterialTheme.typography.bodyMedium,
            color = VoyantColors.TextSecondary
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    options: List<ProfileOption>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            color = VoyantColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )

        options.forEach { option ->
            ProfileOptionItem(option)
            if (option != options.last()) {
                Divider(
                    modifier = Modifier.padding(start = 56.dp),
                    color = VoyantColors.Divider
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileOptionItem(option: ProfileOption) {
    ListItem(
        headlineContent = {
            Text(
                option.title,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                option.subtitle,
                color = VoyantColors.TextSecondary
            )
        },
        leadingContent = {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                tint = VoyantColors.Primary
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = VoyantColors.TextSecondary
            )
        },
        modifier = Modifier.clickable(onClick = option.onClick)
    )
}

private data class ProfileOption(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)