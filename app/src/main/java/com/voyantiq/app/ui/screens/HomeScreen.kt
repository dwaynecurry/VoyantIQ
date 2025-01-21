package com.voyantiq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voyantiq.app.ui.theme.VoyantColors
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTripClick: (String) -> Unit,
    onNewTripClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var userName by remember { mutableStateOf("Dwayne") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Welcome Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            VoyantColors.Primary,
                            VoyantColors.Primary.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Voyant IQ",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Welcome message
                Text(
                    "Welcome back,",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                )
                Text(
                    userName,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Content
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // Quick Action Card
            item {
                Card(
                    onClick = onNewTripClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VoyantColors.Primary.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Plan Your Next Adventure",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Create a new trip itinerary",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }

            // Upcoming Trips Section
            item {
                Text(
                    "Upcoming Trips",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            // Trip Cards
            items(sampleTrips) { trip ->
                TripCard(
                    trip = trip,
                    onClick = { onTripClick(trip.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    val dateFormatter = remember {
        SimpleDateFormat("MMM d", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    val yearFormatter = remember {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    var dateFormatError by remember { mutableStateOf(false) }
    val dateText = remember(trip.startDate, trip.endDate) {
        try {
            "${dateFormatter.format(trip.startDate)} - ${yearFormatter.format(trip.endDate)}"
        } catch (e: Exception) {
            dateFormatError = true
            "Dates unavailable"
        }
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        trip.destination,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        dateText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (dateFormatError) VoyantColors.Error else VoyantColors.TextSecondary
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = VoyantColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = trip.planningProgress,
                modifier = Modifier.fillMaxWidth(),
                color = VoyantColors.Primary,
                trackColor = VoyantColors.Primary.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${(trip.planningProgress * 100).toInt()}% planned",
                style = MaterialTheme.typography.bodySmall,
                color = VoyantColors.TextSecondary
            )
        }
    }
}

data class Trip(
    val id: String,
    val destination: String,
    val startDate: Date,
    val endDate: Date,
    val planningProgress: Float
)

private val sampleTrips = run {
    val calendar = Calendar.getInstance()
    listOf(
        Trip(
            "1",
            "Paris, France",
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 15)
            }.time,
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 7)
            }.time,
            0.8f
        ),
        Trip(
            "2",
            "Tokyo, Japan",
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 45)
            }.time,
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 10)
            }.time,
            0.3f
        ),
        Trip(
            "3",
            "New York City, USA",
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 90)
            }.time,
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, 5)
            }.time,
            0.6f
        )
    )
}