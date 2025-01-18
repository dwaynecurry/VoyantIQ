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
fun TripDetailsScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
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
            IconButton(onClick = onBackClick) {
                Text("←")
            }
            Text(
                text = "Trip Details",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onEditClick) {
                Text("✏️")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Trip Overview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Paris Adventure", // Replace with actual trip name
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "June 15 - June 22, 2024", // Replace with actual dates
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Budget: $2,500", // Replace with actual budget
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Itinerary
            item {
                Text(
                    text = "Itinerary",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Sample itinerary days
            items(7) { dayIndex ->
                DayCard(
                    dayNumber = dayIndex + 1,
                    activities = listOf(
                        "9:00 AM - Breakfast at Local Café",
                        "11:00 AM - Museum Visit",
                        "2:00 PM - Lunch at Restaurant",
                        "4:00 PM - City Tour",
                        "7:00 PM - Dinner"
                    )
                )
            }
        }
    }
}

@Composable
private fun DayCard(
    dayNumber: Int,
    activities: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Day $dayNumber",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            activities.forEach { activity ->
                Text(
                    text = activity,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}