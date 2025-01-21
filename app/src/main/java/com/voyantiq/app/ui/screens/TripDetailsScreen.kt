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
import com.voyantiq.app.data.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var currentDayIndex by remember { mutableStateOf(0) }
    var showAddActivityDialog by remember { mutableStateOf(false) }
    var showTimeConflictDialog by remember { mutableStateOf(false) }
    var conflictingActivity by remember { mutableStateOf<TripActivity?>(null) }

    val trip = remember {
        SampleData.getTripById(tripId) ?: Trip(
            id = "0",
            destination = "Unknown Trip",
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            status = TripStatus.PLANNING,
            progress = 0f,
            budget = 0.0,
            spent = 0.0
        )
    }

    val activities = remember(tripId) {
        SampleData.getActivitiesForTrip(tripId)
    }

    val days = remember(trip) {
        getDaysForTrip(trip)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            trip.destination,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            DateTimeUtils.formatDateRange(trip.startDate, trip.endDate),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                TripOverviewCard(
                    budget = trip.budget,
                    spent = trip.spent
                )
            }

            item {
                ScrollableTabRow(
                    selectedTabIndex = currentDayIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = VoyantColors.Primary
                ) {
                    days.forEachIndexed { index, date ->
                        Tab(
                            selected = currentDayIndex == index,
                            onClick = { currentDayIndex = index }
                        ) {
                            DayTab(
                                date = date,
                                isSelected = currentDayIndex == index
                            )
                        }
                    }
                }
            }

            items(
                activities.filter { activity ->
                    isSameDay(activity.startTime.toLocalDate(), days[currentDayIndex])
                }
            ) { activity ->
                ActivityCard(activity = activity)
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
                    showAddActivityDialog = false
                }
            },
            selectedDate = days[currentDayIndex]
        )
    }

    if (showTimeConflictDialog && conflictingActivity != null) {
        TimeConflictDialog(
            conflictingActivity = conflictingActivity!!,
            onDismiss = { showTimeConflictDialog = false },
            onConfirm = {
                showTimeConflictDialog = false
                showAddActivityDialog = false
            }
        )
    }
}

@Composable
private fun TripOverviewCard(
    budget: Double,
    spent: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Trip Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BudgetInfoColumn(
                    title = "Total Budget",
                    amount = budget
                )
                BudgetInfoColumn(
                    title = "Spent",
                    amount = spent
                )
                BudgetInfoColumn(
                    title = "Remaining",
                    amount = budget - spent,
                    isNegative = spent > budget
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = (spent / budget).toFloat().coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth(),
                color = if (spent > budget)
                    VoyantColors.Error else VoyantColors.Primary,
                trackColor = VoyantColors.Primary.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
private fun BudgetInfoColumn(
    title: String,
    amount: Double,
    isNegative: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium,
            color = VoyantColors.TextSecondary
        )
        Text(
            "$${String.format("%.2f", amount)}",
            style = MaterialTheme.typography.titleMedium,
            color = when {
                isNegative -> VoyantColors.Error
                else -> VoyantColors.Primary
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DayTab(
    date: LocalDate,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = DateTimeUtils.formatDate(date),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) VoyantColors.Primary else VoyantColors.TextSecondary
        )
    }
}

@Composable
private fun ActivityCard(activity: TripActivity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    text = DateTimeUtils.formatTimeRange(activity.startTime, activity.endTime),
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
                if (activity.bookingReference != null) {
                    Text(
                        text = "Booking: ${activity.bookingReference}",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoyantColors.Primary
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
    onAdd: (TripActivity) -> Unit,
    selectedDate: LocalDate
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

                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = type.getTitle(),
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
                            val startTime = selectedDate.atTime(startHour, startMinute)
                            val endTime = selectedDate.atTime(endHour, endMinute)

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
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Time Conflict") },
        text = {
            Text(
                "This time conflicts with: ${conflictingActivity.title}\n" +
                        "(${DateTimeUtils.formatTimeRange(conflictingActivity.startTime, conflictingActivity.endTime)})\n\n" +
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

private fun getDaysForTrip(trip: Trip): List<LocalDate> {
    val days = mutableListOf<LocalDate>()
    var currentDate = trip.startDate
    while (!currentDate.isAfter(trip.endDate)) {
        days.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }
    return days
}

private fun isSameDay(date1: LocalDate, date2: LocalDate): Boolean {
    return date1 == date2
}

private fun checkTimeConflict(newActivity: TripActivity, existingActivities: List<TripActivity>): TripActivity? {
    return existingActivities.find { existing ->
        isSameDay(existing.startTime.toLocalDate(), newActivity.startTime.toLocalDate()) &&
                (
                        (newActivity.startTime.isAfter(existing.startTime) && newActivity.startTime.isBefore(existing.endTime)) ||
                                (newActivity.endTime.isAfter(existing.startTime) && newActivity.endTime.isBefore(existing.endTime)) ||
                                (existing.startTime.isAfter(newActivity.startTime) && existing.startTime.isBefore(newActivity.endTime))
                        )
    }
}