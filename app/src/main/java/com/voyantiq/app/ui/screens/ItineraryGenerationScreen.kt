package com.voyantiq.app.ui.screens

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.voyantiq.app.data.model.Activity
import com.voyantiq.app.data.model.ActivityType
import com.voyantiq.app.data.model.Location
import com.voyantiq.app.ui.components.FlowRow
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryGenerationScreen(
    onBackClick: () -> Unit,
    onSaveItinerary: () -> Unit
) {
    val context = LocalContext.current

    // Initialize OpenStreetMap configuration
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var activities by remember {
        mutableStateOf(
            listOf(
                Activity(
                    id = "1",
                    name = "Breakfast at Cafe Rouge",
                    description = "Start your day with French cuisine",
                    date = LocalDate.now(),
                    time = "9:00 AM",
                    cost = 25.0,
                    location = Location("Cafe Rouge", "123 Main St", 40.7128, -74.0060),
                    type = ActivityType.DINING
                ),
                Activity(
                    id = "2",
                    name = "Central Park Tour",
                    description = "Explore nature in the city",
                    date = LocalDate.now(),
                    time = "11:00 AM",
                    cost = 15.0,
                    location = Location("Central Park", "Central Park West", 40.7829, -73.9654),
                    type = ActivityType.SIGHTSEEING
                )
            )
        )
    }

    var selectedView by remember { mutableStateOf(ViewType.LIST) }
    var showAddActivity by remember { mutableStateOf(false) }
    var selectedActivityType by remember { mutableStateOf<ActivityType?>(null) }
    var showBudgetBreakdown by remember { mutableStateOf(false) }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        activities = activities.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan Your Trip") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    // View type toggle
                    IconButton(onClick = {
                        selectedView = when (selectedView) {
                            ViewType.LIST -> ViewType.MAP
                            ViewType.MAP -> ViewType.TIMELINE
                            ViewType.TIMELINE -> ViewType.LIST
                        }
                    }) {
                        Icon(
                            when (selectedView) {
                                ViewType.LIST -> Icons.Default.Map
                                ViewType.MAP -> Icons.Default.Timeline
                                ViewType.TIMELINE -> Icons.Default.List
                            },
                            "Toggle View"
                        )
                    }

                    // Budget breakdown
                    IconButton(onClick = { showBudgetBreakdown = true }) {
                        Icon(Icons.Default.AccountBalance, "Budget")
                    }

                    // Add activity
                    IconButton(onClick = { showAddActivity = true }) {
                        Icon(Icons.Default.Add, "Add Activity")
                    }

                    // Save itinerary
                    IconButton(onClick = onSaveItinerary) {
                        Icon(Icons.Default.Save, "Save")
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
            // Trip Summary Card
            TripSummaryCard(
                activities = activities,
                selectedActivityType = selectedActivityType,
                onActivityTypeSelected = { selectedActivityType = it }
            )

            when (selectedView) {
                ViewType.LIST -> {
                    LazyColumn(
                        state = state.listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .reorderable(state)
                    ) {
                        items(
                            items = activities.filter {
                                selectedActivityType == null || it.type == selectedActivityType
                            },
                            key = { it.id }
                        ) { activity ->
                            ReorderableItem(state, key = activity.id) { isDragging ->
                                val elevation = animateFloatAsState(if (isDragging) 16f else 1f)

                                ActivityCard(
                                    activity = activity,
                                    modifier = Modifier
                                        .shadow(elevation.value.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                )
                            }
                        }
                    }
                }

                ViewType.MAP -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        // OpenStreetMap View
                        AndroidView(
                            factory = { context ->
                                MapView(context).apply {
                                    setTileSource(TileSourceFactory.MAPNIK)
                                    setMultiTouchControls(true)
                                    controller.setZoom(12.0)

                                    // Add markers for activities
                                    activities.forEach { activity ->
                                        val marker = Marker(this).apply {
                                            position = GeoPoint(activity.location.latitude, activity.location.longitude)
                                            title = activity.name
                                            snippet = activity.description
                                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        }
                                        overlays.add(marker)
                                    }

                                    // Add route line between activities
                                    if (activities.size > 1) {
                                        val routeLine = Polyline().apply {
                                            activities.forEach { activity ->
                                                addPoint(GeoPoint(activity.location.latitude, activity.location.longitude))
                                            }
                                            outlinePaint.color = android.graphics.Color.BLUE
                                            outlinePaint.strokeWidth = 5f
                                        }
                                        overlays.add(routeLine)
                                    }

                                    // Center map on first activity
                                    activities.firstOrNull()?.let { activity ->
                                        controller.setCenter(GeoPoint(activity.location.latitude, activity.location.longitude))
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { mapView ->
                                // Update markers and route when activities change
                                mapView.overlays.clear()

                                activities.forEach { activity ->
                                    val marker = Marker(mapView).apply {
                                        position = GeoPoint(activity.location.latitude, activity.location.longitude)
                                        title = activity.name
                                        snippet = activity.description
                                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    }
                                    mapView.overlays.add(marker)
                                }

                                if (activities.size > 1) {
                                    val routeLine = Polyline().apply {
                                        activities.forEach { activity ->
                                            addPoint(GeoPoint(activity.location.latitude, activity.location.longitude))
                                        }
                                        outlinePaint.color = android.graphics.Color.BLUE
                                        outlinePaint.strokeWidth = 5f
                                    }
                                    mapView.overlays.add(routeLine)
                                }

                                mapView.invalidate()
                            }
                        )

                        // Floating panel with activity list
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(16.dp),
                            shadowElevation = 8.dp,
                            shape = MaterialTheme.shapes.large
                        ) {
                            LazyColumn(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                items(activities) { activity ->
                                    ActivityListItem(activity = activity)
                                }
                            }
                        }
                    }
                }

                ViewType.TIMELINE -> {
                    TimelineView(activities = activities)
                }
            }
        }
    }

    // Dialogs
    if (showAddActivity) {
        AddActivityDialog(
            onDismiss = { showAddActivity = false },
            onActivityAdded = { newActivity ->
                activities = activities + newActivity
                showAddActivity = false
            }
        )
    }

    if (showBudgetBreakdown) {
        BudgetBreakdownDialog(
            onDismiss = { showBudgetBreakdown = false },
            activities = activities
        )
    }
}

@Composable
private fun ActivityListItem(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(activity.name) },
        supportingContent = { Text(activity.location.address) },
        leadingContent = {
            Icon(
                imageVector = getActivityTypeIcon(activity.type),
                contentDescription = null,
                tint = getActivityTypeColor(activity.type)
            )
        },
        trailingContent = {
            Text(
                text = "$${activity.cost}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    )
}

@Composable
private fun TimelineView(activities: List<Activity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(activities.sortedBy { it.time }) { activity ->
            TimelineItem(activity = activity)
        }
    }
}

@Composable
private fun TimelineItem(activity: Activity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Time column
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(80.dp)
        ) {
            Text(
                text = activity.time,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Timeline line
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(2.dp)
                .height(80.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Activity details
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = activity.location.address,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$${activity.cost}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun getActivityTypeIcon(type: ActivityType) = when(type) {
    ActivityType.DINING -> Icons.Default.Restaurant
    ActivityType.SIGHTSEEING -> Icons.Default.Landscape
    ActivityType.ENTERTAINMENT -> Icons.Default.TheaterComedy
    ActivityType.TRANSPORTATION -> Icons.Default.DirectionsCar
    ActivityType.ACCOMMODATION -> Icons.Default.Hotel
}

private fun getActivityTypeColor(type: ActivityType) = when(type) {
    ActivityType.DINING -> Color(0xFF4CAF50)
    ActivityType.SIGHTSEEING -> Color(0xFF2196F3)
    ActivityType.ENTERTAINMENT -> Color(0xFFFFC107)
    ActivityType.TRANSPORTATION -> Color(0xFF9C27B0)
    ActivityType.ACCOMMODATION -> Color(0xFF795548)
}

enum class ViewType {
    LIST,
    MAP,
    TIMELINE
}