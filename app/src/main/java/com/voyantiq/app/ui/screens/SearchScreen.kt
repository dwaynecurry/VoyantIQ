package com.voyantiq.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voyantiq.app.ui.theme.VoyantColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onDestinationClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { },
            active = false,
            onActiveChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search destinations") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        ) { }

        // Popular Destinations
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Popular Destinations",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(sampleDestinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = { onDestinationClick(destination.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DestinationCard(
    destination: Destination,
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
            // Placeholder for destination image
            Surface(
                modifier = Modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.medium),
                color = VoyantColors.Primary.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp),
                    tint = VoyantColors.Primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    destination.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    destination.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoyantColors.TextSecondary
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "View Details",
                tint = VoyantColors.TextSecondary
            )
        }
    }
}

// Data class for destinations
data class Destination(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String? = null
)

// Sample data
private val sampleDestinations = listOf(
    Destination(
        "1",
        "Paris, France",
        "City of Light and Romance",
    ),
    Destination(
        "2",
        "Tokyo, Japan",
        "Where Tradition Meets Innovation",
    ),
    Destination(
        "3",
        "New York City, USA",
        "The City That Never Sleeps",
    ),
    Destination(
        "4",
        "Barcelona, Spain",
        "Art, Architecture, and Mediterranean Charm",
    ),
    Destination(
        "5",
        "Dubai, UAE",
        "Modern Luxury in the Desert",
    )
)