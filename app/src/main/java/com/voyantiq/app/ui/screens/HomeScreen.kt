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
fun HomeScreen(
    onNewTripClick: () -> Unit,
    onTripClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onProfileClick) {
                // Profile Icon placeholder
                Text("👤")
            }
        }

        // New Trip Button
        Button(
            onClick = onNewTripClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Plan New Trip")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Upcoming Trips Section
        Text(
            text = "Upcoming Trips",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Placeholder for upcoming trips
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(3) { index ->
                TripCard(
                    tripName = "Trip ${index + 1}",
                    destination = "Destination ${index + 1}",
                    date = "Date ${index + 1}",
                    onClick = { onTripClick("trip_$index") }
                )
            }
        }
    }
}

@Composable
private fun TripCard(
    tripName: String,
    destination: String,
    date: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = tripName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = destination,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}