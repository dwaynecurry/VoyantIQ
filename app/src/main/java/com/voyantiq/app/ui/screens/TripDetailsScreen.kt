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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.voyantiq.app.ui.theme.VoyantColors
import java.text.SimpleDateFormat
import java.util.*

data class TripActivity(
    val id: String,
    val title: String,
    val type: ActivityType,
    val startTime: Date,
    val endTime: Date,
    val location: String,
    val cost: Double,
    val isBooked: Boolean = false
)

enum class ActivityType {
    RESTAURANT,
    SIGHTSEEING,
    EVENT,
    TRANSPORT;

    fun icon() = when (this) {
        RESTAURANT -> Icons.Default.Restaurant
        SIGHTSEEING -> Icons.Default.Landscape
        EVENT -> Icons.Default.Event
        TRANSPORT -> Icons.Default.DirectionsCar
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var currentDayIndex by remember { mutableStateOf(0) }
    var activities by remember { mutableStateOf(sampleActivities) }
    var showAddActivityDialog by remember { mutableStateOf(false) }
    var showTimeConflictDialog by remember { mutableStateOf(false) }
    var conflictingActivity by remember { mutableStateOf<TripActivity?>(null) }

    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Paris Adventure",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "June 15 - June 22, 2024",
                            style = MaterialTheme.typography.bodyMedium,
                            color = VoyantColors.TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddActivityDialog = true },
                containerColor = VoyantColors.Primary
            ) {
                Icon(Icons.Default.Add, "Add Activity")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Day selector
            ScrollableTabRow(
                selectedTabIndex = currentDayIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = VoyantColors.Background,
                contentColor = VoyantColors.Primary
            ) {
                sampleDays.forEachIndexed { index, day ->
                    Tab(
                        selected = currentDayIndex == index,
                        onClick = { currentDayIndex = index }
                    ) {
                        DayTab(day = day, isSelected = currentDayIndex == index)
                    }
                }
            }

            // Activities list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(
                        activity = activity,
                        timeFormatter = timeFormatter
                    )
                }
            }
        }
    }

    if (showAddActivityDialog) {
        AddActivityDialog(
            onDismiss = { showAddActivityDialog = false },
            onAdd = { newActivity ->
                val conflict = checkTimeConflict(newActivity, activities)
                if (conflict != null) {
                    conflictingActivity = conflict
                    showTimeConflictDialog = true
                } else {
                    activities = activities + newActivity
                    showAddActivityDialog = false
                }
            }
        )
    }

    if (showTimeConflictDialog && conflictingActivity != null) {
        TimeConflictDialog(
            conflictingActivity = conflictingActivity!!,
            timeFormatter = timeFormatter,
            onDismiss = { showTimeConflictDialog = false },
            onConfirm = {
                activities = activities + (conflictingActivity ?: return@TimeConflictDialog)
                showTimeConflictDialog = false
                showAddActivityDialog = false
            }
        )
    }
}

@Composable
private fun DayTab(day: Date, isSelected: Boolean) {
    val dayFormatter = remember {
        SimpleDateFormat("EEE", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }
    val dateFormatter = remember {
        SimpleDateFormat("MMM d", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = dayFormatter.format(day),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) VoyantColors.Primary else VoyantColors.TextSecondary
        )
        Text(
            text = dateFormatter.format(day),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) VoyantColors.Primary else VoyantColors.TextSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityCard(
    activity: TripActivity,
    timeFormatter: SimpleDateFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                imageVector = activity.type.icon(),
                contentDescription = null,
                tint = VoyantColors.Primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${timeFormatter.format(activity.startTime)} - " +
                            "${timeFormatter.format(activity.endTime)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoyantColors.TextSecondary
                )
                if (activity.location.isNotEmpty()) {
                    Text(
                        text = activity.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = VoyantColors.TextSecondary
                    )
                }
            }

            Text(
                text = "$${activity.cost}",
                style = MaterialTheme.typography.titleMedium,
                color = VoyantColors.Primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AddActivityDialog(
    onDismiss: () -> Unit,
    onAdd: (TripActivity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ActivityType.SIGHTSEEING) }
    var startHour by remember { mutableStateOf(9) }
    var startMinute by remember { mutableStateOf(0) }
    var endHour by remember { mutableStateOf(10) }
    var endMinute by remember { mutableStateOf(0) }
    var location by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("0.00") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Add Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Activity Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Activity Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = type.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Activity Type") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier.menuAnchor()
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cost,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            cost = it
                        }
                    },
                    label = { Text("Cost ($)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val calendar = Calendar.getInstance()

                            // Set start time
                            calendar.set(Calendar.HOUR_OF_DAY, startHour)
                            calendar.set(Calendar.MINUTE, startMinute)
                            val startTime = calendar.time

                            // Set end time
                            calendar.set(Calendar.HOUR_OF_DAY, endHour)
                            calendar.set(Calendar.MINUTE, endMinute)
                            val endTime = calendar.time

                            val newActivity = TripActivity(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                type = type,
                                startTime = startTime,
                                endTime = endTime,
                                location = location,
                                cost = cost.toDoubleOrNull() ?: 0.0
                            )
                            onAdd(newActivity)
                        },
                        enabled = title.isNotBlank() && location.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeConflictDialog(
    conflictingActivity: TripActivity,
    timeFormatter: SimpleDateFormat,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Time Conflict") },
        text = {
            Text(
                "This time conflicts with: ${conflictingActivity.title}\n" +
                        "(${timeFormatter.format(conflictingActivity.startTime)} - " +
                        "${timeFormatter.format(conflictingActivity.endTime)})\n\n" +
                        "Would you like to add it anyway?"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Add Anyway")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun checkTimeConflict(newActivity: TripActivity, existingActivities: List<TripActivity>): TripActivity? {
    return existingActivities.find { existing ->
        (newActivity.startTime.time in existing.startTime.time..existing.endTime.time) ||
                (newActivity.endTime.time in existing.startTime.time..existing.endTime.time) ||
                (existing.startTime.time in newActivity.startTime.time..newActivity.endTime.time)
    }
}

// Sample data
private val sampleDays = run {
    val calendar = Calendar.getInstance()
    List(7) { index ->
        calendar.apply {
            add(Calendar.DAY_OF_MONTH, if (index == 0) 0 else 1)
        }.time
    }
}

private val sampleActivities = run {
    val calendar = Calendar.getInstance()
    listOf(
        TripActivity(
            "1",
            "Eiffel Tower Visit",
            ActivityType.SIGHTSEEING,
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
            }.time,
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
            }.time,
            "Champ de Mars, 5 Avenue Anatole France",
            25.0
        ),
        TripActivity(
            "2",
            "Lunch at Le Cheval d'Or",
            ActivityType.RESTAURANT,
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 30)
            }.time,
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 14)
                set(Calendar.MINUTE, 0)
            }.time,
            "21 Rue de la Villette",
            45.0
        )
    )
}