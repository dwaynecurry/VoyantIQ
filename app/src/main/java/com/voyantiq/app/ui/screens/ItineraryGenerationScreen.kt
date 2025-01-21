package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItineraryGenerationScreen(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    var isGenerating by remember { mutableStateOf(true) }

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
                text = "Your Itinerary",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (isGenerating) {
            // Loading State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Generating your perfect itinerary...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // Generated Itinerary
            LazyColumn(
                modifier = Modifier.weight(1f),
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
                                text = "Trip Overview",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text("Duration: 7 days")
                            Text("Total Budget: $2,500")
                            Text("Activities: 15")
                        }
                    }
                }

                // Generated Activities
                items(5) { index ->
                    ActivityCard(
                        title = "Day ${index + 1}",
                        description = "Various activities planned",
                        time = "Full day",
                        cost = "$200"
                    )
                }
            }

            // Confirm Button
            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Confirm Itinerary")
            }
        }
    }
}

@Composable
private fun ActivityCard(
    title: String,
    description: String,
    time: String,
    cost: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = cost,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}