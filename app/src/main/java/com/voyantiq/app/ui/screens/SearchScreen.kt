package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onDestinationClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isSearching = it.isNotEmpty()
                },
                placeholder = { Text("Search destinations") },
                modifier = Modifier.weight(1f)
            )
        }

        if (isSearching) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleDestinations.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }) { destination ->
                    DestinationItem(
                        destination = destination,
                        onClick = { onDestinationClick(destination.name) }
                    )
                }
            }
        } else {
            Text(
                text = "Popular Destinations",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleDestinations) { destination ->
                    DestinationItem(
                        destination = destination,
                        onClick = { onDestinationClick(destination.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DestinationItem(
    destination: DestinationData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = destination.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = destination.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private data class DestinationData(
    val name: String,
    val description: String
)

private val sampleDestinations = listOf(
    DestinationData("Paris, France", "The City of Light"),
    DestinationData("Tokyo, Japan", "Modern meets traditional"),
    DestinationData("New York, USA", "The city that never sleeps"),
    DestinationData("Rome, Italy", "Eternal city of history"),
    DestinationData("Sydney, Australia", "Harbor city paradise")
)