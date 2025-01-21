package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.voyantiq.app.ui.theme.VoyantColors
import com.voyantiq.app.data.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TripPlanningState(
    val destination: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val budget: String = "",
    val travelers: String = "1",
    val travelStyle: TravelStyle = TravelStyle.BALANCED,
    val interests: List<TravelInterest> = emptyList()
)

enum class TravelStyle {
    LUXURY,
    BALANCED,
    BUDGET,
    BACKPACKER;

    fun getTitle(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

enum class TravelInterest {
    CULTURE,
    FOOD,
    ADVENTURE,
    RELAXATION,
    SHOPPING,
    NATURE,
    HISTORY,
    NIGHTLIFE;

    fun getTitle(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TripPlanningScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var planningState by remember { mutableStateOf(TripPlanningState()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingStartDate by remember { mutableStateOf(true) }

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
            // Destination
            OutlinedTextField(
                value = planningState.destination,
                onValueChange = { planningState = planningState.copy(destination = it) },
                label = { Text("Where do you want to go?*") },
                placeholder = { Text("Enter destination") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null)
                },
                supportingText = {
                    Text("Popular: Paris, Tokyo, New York")
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
                    }
                )
            }

            // Budget
            OutlinedTextField(
                value = planningState.budget,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                        planningState = planningState.copy(budget = it)
                    }
                },
                label = { Text("Total Budget*") },
                placeholder = { Text("Enter your budget") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.AttachMoney, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    Text("Recommended: $2000-$5000 for a week")
                }
            )

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
                onClick = onNextClick,
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
            // Date picker implementation
            Text("Date Picker placeholder - will implement with actual date picker")
        }
    }
}

private fun isFormValid(state: TripPlanningState): Boolean {
    return state.destination.isNotBlank() &&
            state.startDate != null &&
            state.endDate != null &&
            state.budget.isNotBlank() &&
            state.travelers.isNotBlank() &&
            state.interests.isNotEmpty()
}