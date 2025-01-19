package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.voyantiq.app.data.model.Activity
import com.voyantiq.app.data.model.ActivityType
import com.voyantiq.app.ui.components.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDiscoveryScreen(
    onBackClick: () -> Unit,
    onActivitySelected: (Activity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<ActivityType?>(null) }
    var selectedPriceRange by remember { mutableStateOf<PriceRange?>(null) }
    var selectedTimeOfDay by remember { mutableStateOf<TimeOfDay?>(null) }

    // Simulated recommended activities
    val recommendedActivities = remember {
        generateSampleActivities()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover Activities") },
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
                placeholder = { Text("Search activities, places, or categories") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            ) { }

            // Filters
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Activity Types Filter
                    Text(
                        "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        mainAxisSpacing = 8,
                        crossAxisSpacing = 8
                    ) {
                        ActivityType.values().forEach { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = {
                                    selectedType = if (selectedType == type) null else type
                                },
                                label = { Text(type.name.replace("_", " ")) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getActivityTypeIcon(type),
                                        contentDescription = null,
                                        tint = getActivityTypeColor(type)
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    // Price Range Filter
                    Text(
                        "Price Range",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        mainAxisSpacing = 8,
                        crossAxisSpacing = 8
                    ) {
                        PriceRange.values().forEach { range ->
                            FilterChip(
                                selected = selectedPriceRange == range,
                                onClick = {
                                    selectedPriceRange = if (selectedPriceRange == range) null else range
                                },
                                label = { Text(range.symbol) }
                            )
                        }
                    }
                }

                item {
                    // Time of Day Filter
                    Text(
                        "Time of Day",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        mainAxisSpacing = 8,
                        crossAxisSpacing = 8
                    ) {
                        TimeOfDay.values().forEach { time ->
                            FilterChip(
                                selected = selectedTimeOfDay == time,
                                onClick = {
                                    selectedTimeOfDay = if (selectedTimeOfDay == time) null else time
                                },
                                label = { Text(time.label) },
                                leadingIcon = { Icon(time.icon, contentDescription = null) }
                            )
                        }
                    }
                }

                item {
                    // Recommended Activities Grid
                    Text(
                        "Recommended for You",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(recommendedActivities.filter {
                            (selectedType == null || it.type == selectedType) &&
                                    (selectedPriceRange == null || getPriceRange(it.cost) == selectedPriceRange) &&
                                    (selectedTimeOfDay == null || getTimeOfDay(it.time) == selectedTimeOfDay) &&
                                    (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
                        }) { activity ->
                            ActivityCard(
                                activity = activity,
                                onClick = { onActivitySelected(activity) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityCard(
    activity: Activity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = getActivityTypeIcon(activity.type),
                contentDescription = null,
                tint = getActivityTypeColor(activity.type),
                modifier = Modifier
                    .size(32.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = activity.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = activity.location.address,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${activity.cost}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = activity.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

enum class PriceRange(val symbol: String, val range: ClosedRange<Double>) {
    LOW("$", 0.0..25.0),
    MEDIUM("$$", 25.0..50.0),
    HIGH("$$$", 50.0..100.0),
    VERY_HIGH("$$$$", 100.0..Double.POSITIVE_INFINITY)
}

enum class TimeOfDay(val label: String, val icon: ImageVector, val timeRange: String) {
    MORNING("Morning", Icons.Default.WbSunny, "6:00 AM - 11:59 AM"),
    AFTERNOON("Afternoon", Icons.Default.LightMode, "12:00 PM - 4:59 PM"),
    EVENING("Evening", Icons.Default.Brightness3, "5:00 PM - 8:59 PM"),
    NIGHT("Night", Icons.Default.DarkMode, "9:00 PM - 5:59 AM")
}

private fun getPriceRange(cost: Double): PriceRange =
    PriceRange.values().first { cost in it.range }

private fun getTimeOfDay(time: String): TimeOfDay {
    // Implement time parsing logic
    return TimeOfDay.values().random()
}

private fun generateSampleActivities(): List<Activity> {
    // Implement sample data generation
    return emptyList()
}