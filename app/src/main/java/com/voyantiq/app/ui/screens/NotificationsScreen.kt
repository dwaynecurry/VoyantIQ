package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen(
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
                text = "Notifications",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { /* Clear all */ }) {
                Text("🗑️")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleNotifications) { notification ->
                NotificationItem(notification)
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: NotificationData
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = notification.time,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

private data class NotificationData(
    val title: String,
    val message: String,
    val time: String
)

private val sampleNotifications = listOf(
    NotificationData(
        "Trip Reminder",
        "Your trip to Paris starts in 3 days!",
        "2 hours ago"
    ),
    NotificationData(
        "Booking Confirmation",
        "Your hotel booking has been confirmed",
        "5 hours ago"
    ),
    NotificationData(
        "Price Alert",
        "Flight prices have dropped for your saved route",
        "1 day ago"
    )
)