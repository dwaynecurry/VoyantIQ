package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.rememberScrollState
import com.voyantiq.app.data.model.TripPlanningState
import com.voyantiq.app.data.model.TravelInterest
import com.voyantiq.app.data.model.TravelStyle
import com.voyantiq.app.network.NetworkModule
import com.voyantiq.app.navigation.api.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlanningScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var planningState by remember { mutableStateOf(TripPlanningState()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingStartDate by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Origin and Destination
            OutlinedTextField(
                value = planningState.origin,
                onValueChange = { planningState = planningState.copy(origin = it) },
                label = { Text("Origin*") },
                placeholder = { Text("Enter origin") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null)
                }
            )

            OutlinedTextField(
                value = planningState.destination,
                onValueChange = { planningState = planningState.copy(destination = it) },
                label = { Text("Destination*") },
                placeholder = { Text("Enter destination") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null)
                }
            )
            // Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = planningState.startDate?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: "",
                    onValueChange = { },
                    label = { Text("Start Date*") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isSelectingStartDate = true
                            showDatePicker = true
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Start Date")
                        }
                    }
                )

                OutlinedTextField(
                    value = planningState.endDate?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: "",
                    onValueChange = { },
                    label = { Text("End Date*") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isSelectingStartDate = false
                            showDatePicker = true
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select End Date")
                        }
                    }
                )
            }

            // Budget Range
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = planningState.budgetMin,
                    onValueChange = { planningState = planningState.copy(budgetMin = it) },
                    label = { Text("Min Budget*") },
                    placeholder = { Text("Enter min budget") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = planningState.budgetMax,
                    onValueChange = { planningState = planningState.copy(budgetMax = it) },
                    label = { Text("Max Budget*") },
                    placeholder = { Text("Enter Max Budget") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            // Number of Travelers
            OutlinedTextField(
                value = planningState.travelers,
                onValueChange = {
                    if (it.matches(Regex("^[1-9]\\d*$")) || it.isEmpty()) {
                        planningState = planningState.copy(travelers = it)
                    }
                },
                label = { Text("Number of Travelers*") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Group, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Travel Style
            Text(
                "Travel Style",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TravelStyle.values().forEach { style ->
                    FilterChip(
                        selected = style == planningState.travelStyle,
                        onClick = {
                            planningState = planningState.copy(travelStyle = style)
                        },
                        label = { Text(style.getTitle()) }
                    )
                }
            }

            // Interests
            Text(
                "Interests",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TravelInterest.values().forEach { interest ->
                    FilterChip(
                        selected = interest in planningState.interests,
                        onClick = {
                            val newInterests = planningState.interests.toMutableList()
                            if (interest in planningState.interests) {
                                newInterests.remove(interest)
                            } else {
                                newInterests.add(interest)
                            }
                            planningState = planningState.copy(interests = newInterests)
                        },
                        label = { Text(interest.getTitle()) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Next Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        // Example API calls
                        val tripPlanResponse = NetworkModule.aiTripPlannerApi.generateTripPlan(
                            TripPlanRequest(
                                days = 7,
                                destination = planningState.destination,
                                interests = planningState.interests.map { it.name },
                                budget = planningState.budgetMax,
                                travelMode = "public transport"
                            )
                        )
                        if (tripPlanResponse.isSuccessful) {
                            // Handle successful response
                            onNextClick()
                        } else {
                            // Handle error
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid(planningState)
            ) {
                Text("Continue to Itinerary")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            // Implement a date picker here
            // Example: Use a library like MaterialDatePicker
        }
    }
}

private fun isFormValid(state: TripPlanningState): Boolean {
    return state.origin.isNotBlank() &&
            state.destination.isNotBlank() &&
            state.startDate != null &&
            state.endDate != null &&
            state.budgetMin.isNotBlank() &&
            state.budgetMax.isNotBlank() &&
            state.travelers.isNotBlank() &&
            state.interests.isNotEmpty()
}