package com.voyantiq.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class TripType {
    LEISURE, BUSINESS, ADVENTURE, CULTURAL, LUXURY
}

enum class ActivityPreference {
    SIGHTSEEING, FOOD_DINING, SHOPPING, NATURE, NIGHTLIFE, MUSEUMS, SPORTS
}

data class TripPreferences(
    val tripType: TripType = TripType.LEISURE,
    val numberOfTravelers: Int = 1,
    val activityPreferences: Set<ActivityPreference> = emptySet(),
    val pacePreference: Float = 0.5f, // 0.0 relaxed to 1.0 intensive
    val includeLocalTransport: Boolean = true,
    val includeMeals: Boolean = true
)

@Composable
fun TripPlanningScreen(
    onBackClick: () -> Unit,
    onNextClick: (TripDetails) -> Unit
) {
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var budget by remember { mutableStateOf("") }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(0) }
    var tripPreferences by remember { mutableStateOf(TripPreferences()) }

    val scrollState = rememberScrollState()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Plan Your Trip") },
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
                .verticalScroll(scrollState)
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = currentStep / 3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Basic Details
            AnimatedVisibility(visible = currentStep == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Where are you heading?",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = destination,
                        onValueChange = { destination = it },
                        label = { Text("Destination") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = startDate.format(dateFormatter),
                            onValueChange = { },
                            label = { Text("Start Date") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showStartDatePicker = true },
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = endDate.format(dateFormatter),
                            onValueChange = { },
                            label = { Text("End Date") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showEndDatePicker = true },
                            readOnly = true
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { currentStep = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = destination.isNotBlank()
                    ) {
                        Text("Next: Trip Type")
                    }
                }
            }

            // Step 2: Trip Type and Travelers
            AnimatedVisibility(visible = currentStep == 1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "What type of trip is this?",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(Modifier.selectableGroup()) {
                        TripType.values().forEach { tripType ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = tripType == tripPreferences.tripType,
                                        onClick = {
                                            tripPreferences = tripPreferences.copy(tripType = tripType)
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = tripType == tripPreferences.tripType,
                                    onClick = null
                                )
                                Text(
                                    text = tripType.name.replace("_", " "),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Number of Travelers",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                if (tripPreferences.numberOfTravelers > 1) {
                                    tripPreferences = tripPreferences.copy(
                                        numberOfTravelers = tripPreferences.numberOfTravelers - 1
                                    )
                                }
                            }
                        ) {
                            Icon(Icons.Default.Remove, "Decrease")
                        }

                        Text(
                            text = "${tripPreferences.numberOfTravelers}",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        IconButton(
                            onClick = {
                                tripPreferences = tripPreferences.copy(
                                    numberOfTravelers = tripPreferences.numberOfTravelers + 1
                                )
                            }
                        ) {
                            Icon(Icons.Default.Add, "Increase")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { currentStep = 0 }
                        ) {
                            Text("Back")
                        }

                        Button(
                            onClick = { currentStep = 2 }
                        ) {
                            Text("Next: Preferences")
                        }
                    }
                }
            }

            // Step 3: Preferences and Budget
            AnimatedVisibility(visible = currentStep == 2) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "What would you like to do?",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Activity preferences
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        ActivityPreference.values().forEach { preference ->
                            FilterChip(
                                selected = tripPreferences.activityPreferences.contains(preference),
                                onClick = {
                                    tripPreferences = if (tripPreferences.activityPreferences.contains(preference)) {
                                        tripPreferences.copy(activityPreferences = tripPreferences.activityPreferences - preference)
                                    } else {
                                        tripPreferences.copy(activityPreferences = tripPreferences.activityPreferences + preference)
                                    }
                                },
                                label = {
                                    Text(preference.name.replace("_", " "))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Trip Pace",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Slider(
                        value = tripPreferences.pacePreference,
                        onValueChange = {
                            tripPreferences = tripPreferences.copy(pacePreference = it)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Relaxed", color = Color.Gray)
                        Text("Intensive", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Budget section
                    OutlinedTextField(
                        value = budget,
                        onValueChange = { if (it.all { char -> char.isDigit() }) budget = it },
                        label = { Text("Budget (USD)") },
                        leadingIcon = { Icon(Icons.Default.MonetizationOn, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number,
                        prefix = { Text("$") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Additional options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Include Local Transport")
                        Switch(
                            checked = tripPreferences.includeLocalTransport,
                            onCheckedChange = {
                                tripPreferences = tripPreferences.copy(includeLocalTransport = it)
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Include Meals")
                        Switch(
                            checked = tripPreferences.includeMeals,
                            onCheckedChange = {
                                tripPreferences = tripPreferences.copy(includeMeals = it)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { currentStep = 1 }
                        ) {
                            Text("Back")
                        }

                        Button(
                            onClick = {
                                onNextClick(
                                    TripDetails(
                                        destination = destination,
                                        startDate = startDate.toString(),
                                        endDate = endDate.toString(),
                                        budget = budget,
                                        preferences = tripPreferences
                                    )
                                )
                            },
                            enabled = isFormValid(destination, budget, tripPreferences)
                        ) {
                            Text("Create Itinerary")
                        }
                    }
                }
            }
        }
    }

    // Date Pickers (same as before)
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = startDate.toEpochDay() * 24 * 60 * 60 * 1000
                )
            )
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = endDate.toEpochDay() * 24 * 60 * 60 * 1000
                )
            )
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Int = 0,
    crossAxisSpacing: Int = 0,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = mutableListOf<List<Pair<Int, Int>>>()
        val rowWidths = mutableListOf<Int>()
        val rowHeights = mutableListOf<Int>()

        var currentRow = mutableListOf<Pair<Int, Int>>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentRowWidth + placeable.width +
                (if (currentRow.isEmpty()) 0 else mainAxisSpacing) > constraints.maxWidth) {
                rows.add(currentRow.toList())
                rowWidths.add(currentRowWidth)
                rowHeights.add(currentRowHeight)

                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowHeight = 0
            }

            currentRow.add(Pair(placeable.width, placeable.height))
            currentRowWidth += placeable.width + (if (currentRow.size > 1) mainAxisSpacing else 0)
            currentRowHeight = maxOf(currentRowHeight, placeable.height)
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowWidths.add(currentRowWidth)
            rowHeights.add(currentRowHeight)
        }

        val totalHeight = rowHeights.sum() + (rows.size - 1) * crossAxisSpacing

        layout(constraints.maxWidth, totalHeight) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = 0
                row.forEach { (width, height) ->
                    placeRelative(x, y)
                    x += width + mainAxisSpacing
                }
                y += rowHeights[rowIndex] + crossAxisSpacing
            }
        }
    }
}

private fun isFormValid(
    destination: String,
    budget: String,
    preferences: TripPreferences
): Boolean {
    return destination.isNotBlank() &&
            budget.isNotBlank() &&
            preferences.activityPreferences.isNotEmpty()
}

data class TripDetails(
    val destination: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val preferences: TripPreferences
)