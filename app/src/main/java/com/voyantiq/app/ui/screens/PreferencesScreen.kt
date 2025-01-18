package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voyantiq.app.data.model.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    onBackClick: () -> Unit,
    onPreferencesSaved: () -> Unit
) {
    var preferences by remember { mutableStateOf(UserPreferences()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Your Preferences") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Travel Style Section
            item {
                PreferenceSection(
                    title = "Travel Style",
                    options = listOf(
                        "Luxury",
                        "Adventure",
                        "Cultural",
                        "Relaxation",
                        "Urban",
                        "Eco-friendly"
                    ),
                    selectedOptions = preferences.travelStyle,
                    onSelectionChanged = {
                        preferences = preferences.copy(travelStyle = it)
                    }
                )
            }

            // Budget Range Section
            item {
                Text(
                    text = "Budget Range",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column {
                    listOf(
                        "Budget ($0-$100/day)",
                        "Moderate ($100-$300/day)",
                        "Luxury ($300+/day)"
                    ).forEach { range ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = preferences.budgetRange == range,
                                onClick = {
                                    preferences = preferences.copy(budgetRange = range)
                                }
                            )
                            Text(
                                text = range,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            // Activities Section
            item {
                PreferenceSection(
                    title = "Activities",
                    options = listOf(
                        "Sightseeing",
                        "Museums",
                        "Shopping",
                        "Outdoor Activities",
                        "Food Tours",
                        "Local Experiences"
                    ),
                    selectedOptions = preferences.preferredActivities,
                    onSelectionChanged = {
                        preferences = preferences.copy(preferredActivities = it)
                    }
                )
            }

            // Food Preferences Section
            item {
                PreferenceSection(
                    title = "Food Preferences",
                    options = listOf(
                        "Local Cuisine",
                        "Fine Dining",
                        "Street Food",
                        "Casual Dining",
                        "Food Markets",
                        "Cooking Classes"
                    ),
                    selectedOptions = preferences.foodPreferences,
                    onSelectionChanged = {
                        preferences = preferences.copy(foodPreferences = it)
                    }
                )
            }

            // Dietary Restrictions Section
            item {
                PreferenceSection(
                    title = "Dietary Restrictions",
                    options = listOf(
                        "Vegetarian",
                        "Vegan",
                        "Gluten-free",
                        "Halal",
                        "Kosher",
                        "None"
                    ),
                    selectedOptions = preferences.dietaryRestrictions,
                    onSelectionChanged = {
                        preferences = preferences.copy(dietaryRestrictions = it)
                    }
                )
            }

            // Save Button
            item {
                Button(
                    onClick = onPreferencesSaved,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = isPreferencesValid(preferences)
                ) {
                    Text("Save Preferences")
                }
            }
        }
    }
}

@Composable
private fun PreferenceSection(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = { checked ->
                        if (checked) {
                            onSelectionChanged(selectedOptions + option)
                        } else {
                            onSelectionChanged(selectedOptions - option)
                        }
                    }
                )
                Text(
                    text = option,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

private fun isPreferencesValid(preferences: UserPreferences): Boolean {
    return preferences.travelStyle.isNotEmpty() &&
            preferences.budgetRange.isNotEmpty() &&
            preferences.preferredActivities.isNotEmpty()
}