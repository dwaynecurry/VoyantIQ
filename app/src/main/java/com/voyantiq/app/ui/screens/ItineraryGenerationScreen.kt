package com.voyantiq.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.voyantiq.app.data.model.SampleData
import com.voyantiq.app.data.model.TripActivity
import com.voyantiq.app.data.model.DateTimeUtils

@Composable
fun ItineraryGenerationScreen(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    var expandedDay by remember { mutableStateOf<Int?>(null) }
    var activities by remember { mutableStateOf(SampleData.getActivitiesForTrip("1")) }
    val days = activities.groupBy { it.startTime.toLocalDate() }

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
                text = "Your Itinerary",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Itinerary Overview
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(activities) { index, activity ->
                ActivityCard(
                    activity = activity,
                    onDrag = { fromIndex, toIndex ->
                        activities = activities.toMutableList().apply {
                            add(toIndex, removeAt(fromIndex))
                        }
                    },
                    index = index
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

@Composable
private fun ActivityCard(
    activity: TripActivity,
    onDrag: (Int, Int) -> Unit,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Handle drag logic here
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
    }
}