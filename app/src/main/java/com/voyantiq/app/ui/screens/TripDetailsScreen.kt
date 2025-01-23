package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voyantiq.app.data.model.SampleData
import com.voyantiq.app.data.model.Trip
import com.voyantiq.app.data.model.TripActivity
import com.voyantiq.app.data.model.DateTimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: String,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val trip = SampleData.getTripById(tripId)
    val activities = SampleData.getActivitiesForTrip(tripId)

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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = trip?.destination ?: "Trip Details",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        // Trip Overview
        trip?.let {
            TripOverview(trip = it)
        }

        // Activities List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(activities) { activity ->
                ActivityCard(activity = activity)
            }
        }
    }
}

@Composable
private fun TripOverview(trip: Trip) {
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
            Text("Duration: ${DateTimeUtils.formatDateRange(trip.startDate, trip.endDate)}")
            Text("Status: ${trip.status}")
            Text("Budget: $${trip.budget}")
        }
    }
}
@Composable
private fun ActivityCard(activity: TripActivity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = activity.type.getIcon(),
                contentDescription = activity.type.getTitle(),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${DateTimeUtils.formatTime(activity.startTime)} - ${DateTimeUtils.formatTime(activity.endTime)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = activity.location,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "$${activity.cost}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
// Additional Components or Functions

// You can add more components here if needed, such as dialogs for editing activities or trip details.