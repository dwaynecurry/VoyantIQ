package com.voyantiq.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voyantiq.app.network.NetworkModule
import com.voyantiq.app.network.models.Restaurant
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onEventClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var activities by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                coroutineScope.launch {
                    val response = NetworkModule.tripadvisorApi.searchRestaurants(locationId = "304554")
                    if (response.isSuccessful) {
                        activities = response.body()?.restaurants ?: emptyList()
                    }
                }
            },
            active = false,
            onActiveChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search for events, dining, etc.") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        ) { }

        // Quick Search Results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Quick Finds for Today",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(activities) { activity ->
                ActivityCard(
                    activity = activity,
                    onClick = { onEventClick(activity.id) }
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: Restaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    activity.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    activity.cuisine.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
