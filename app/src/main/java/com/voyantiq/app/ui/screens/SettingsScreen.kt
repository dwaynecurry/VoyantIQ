package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Text("←")
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingSection(
                    title = "Account",
                    settings = listOf(
                        SettingItem("Personal Information", {}),
                        SettingItem("Email Preferences", {}),
                        SettingItem("Password", {})
                    )
                )
            }

            item {
                SettingSection(
                    title = "Preferences",
                    settings = listOf(
                        SettingItem("Currency", {}),
                        SettingItem("Language", {}),
                        SettingItem("Time Zone", {})
                    )
                )
            }

            item {
                SettingSection(
                    title = "Notifications",
                    settings = listOf(
                        SettingItem("Push Notifications", {}),
                        SettingItem("Email Notifications", {}),
                        SettingItem("Trip Reminders", {})
                    )
                )
            }

            item {
                SettingSection(
                    title = "Privacy & Security",
                    settings = listOf(
                        SettingItem("Privacy Settings", {}),
                        SettingItem("Two-Factor Authentication", {}),
                        SettingItem("Data Usage", {})
                    )
                )
            }

            item {
                SettingSection(
                    title = "Support",
                    settings = listOf(
                        SettingItem("Help Center", {}),
                        SettingItem("Contact Support", {}),
                        SettingItem("Terms of Service", {}),
                        SettingItem("Privacy Policy", {})
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    settings: List<SettingItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            settings.forEach { setting ->
                SettingRow(setting)
            }
        }
    }
}

@Composable
private fun SettingRow(
    setting: SettingItem
) {
    TextButton(
        onClick = setting.onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(setting.title)
            Text("→")
        }
    }
}

private data class SettingItem(
    val title: String,
    val onClick: () -> Unit
)