package com.voyantiq.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.voyantiq.app.data.model.PreferenceOption
import com.voyantiq.app.data.model.PreferenceStep
import com.voyantiq.app.data.model.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernUserPreferencesScreen(
    onPreferencesSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    var preferences by remember { mutableStateOf(UserPreferences()) }

    val steps = remember {
        listOf(
            PreferenceStep(
                title = "Travel Style",
                icon = Icons.Default.FlightTakeoff,
                content = { @Composable { prefs: UserPreferences ->
                    TravelStyleSection(
                        preferences = prefs,
                        onPreferencesChanged = { preferences = it }
                    )
                } }
            ),
            PreferenceStep(
                title = "Budget",
                icon = Icons.Default.AccountBalance,
                content = { @Composable { prefs: UserPreferences ->
                    BudgetSection(
                        preferences = prefs,
                        onPreferencesChanged = { preferences = it }
                    )
                } }
            ),
            PreferenceStep(
                title = "Activities",
                icon = Icons.Default.LocalActivity,
                content = { @Composable { prefs: UserPreferences ->
                    ActivitiesSection(
                        preferences = prefs,
                        onPreferencesChanged = { preferences = it }
                    )
                } }
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Personalize Your Experience",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = (currentStep + 1) / steps.size.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Step Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                steps.forEachIndexed { index, step ->
                    StepIndicator(
                        step = index,
                        currentStep = currentStep,
                        icon = step.icon,
                        title = step.title
                    )
                }
            }

            // Content
            Box(modifier = Modifier.weight(1f)) {
                steps[currentStep].content(preferences)
            }

            // Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- }
                    ) {
                        Text("Previous")
                    }
                }

                if (currentStep < steps.size - 1) {
                    Button(
                        onClick = { currentStep++ }
                    ) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = onPreferencesSaved,
                        enabled = isPreferencesValid(preferences)
                    ) {
                        Text("Complete Setup")
                    }
                }
            }
        }
    }
}

@Composable
private fun TravelStyleSection(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    val travelStyles = listOf(
        PreferenceOption(
            "Luxury",
            Icons.Default.Star,
            "High-end experiences"
        ),
        PreferenceOption(
            "Adventure",
            Icons.Default.Hiking,
            "Outdoor activities"
        ),
        PreferenceOption(
            "Cultural",
            Icons.Default.Museum,
            "Local experiences"
        ),
        PreferenceOption(
            "Relaxation",
            Icons.Default.BeachAccess,
            "Peaceful getaways"
        ),
        PreferenceOption(
            "Urban",
            Icons.Default.LocationCity,
            "City exploration"
        ),
        PreferenceOption(
            "Eco-friendly",
            Icons.Default.Park,
            "Sustainable travel"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "What's your travel style?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(travelStyles) { style ->
                PreferenceCard(
                    option = style,
                    isSelected = preferences.travelStyle.contains(style.title),
                    onSelect = { selected ->
                        val newStyles = if (selected) {
                            preferences.travelStyle + style.title
                        } else {
                            preferences.travelStyle - style.title
                        }
                        onPreferencesChanged(preferences.copy(travelStyle = newStyles))
                    }
                )
            }
        }
    }
}

@Composable
private fun BudgetSection(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    val budgetRanges = listOf(
        PreferenceOption(
            "Budget",
            Icons.Default.MoneyOff,
            "$0-$100 per day"
        ),
        PreferenceOption(
            "Moderate",
            Icons.Default.AccountBalance,
            "$100-$300 per day"
        ),
        PreferenceOption(
            "Luxury",
            Icons.Default.Diamond,
            "$300+ per day"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "What's your daily budget?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        budgetRanges.forEach { range ->
            BudgetRangeCard(
                option = range,
                isSelected = preferences.budgetRange == range.title,
                onSelect = {
                    onPreferencesChanged(preferences.copy(budgetRange = range.title))
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ActivitiesSection(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    val activities = listOf(
        PreferenceOption(
            "Sightseeing",
            Icons.Default.PhotoCamera,
            "Tourist attractions"
        ),
        PreferenceOption(
            "Food & Dining",
            Icons.Default.Restaurant,
            "Local cuisine"
        ),
        PreferenceOption(
            "Shopping",
            Icons.Default.ShoppingBag,
            "Markets and retail"
        ),
        PreferenceOption(
            "Nature",
            Icons.Default.Landscape,
            "Outdoor activities"
        ),
        PreferenceOption(
            "Nightlife",
            Icons.Default.Nightlife,
            "Entertainment"
        ),
        PreferenceOption(
            "Wellness",
            Icons.Default.Spa,
            "Spa and relaxation"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "What activities interest you?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(activities) { activity ->
                PreferenceCard(
                    option = activity,
                    isSelected = preferences.preferredActivities.contains(activity.title),
                    onSelect = { selected ->
                        val newActivities = if (selected) {
                            preferences.preferredActivities + activity.title
                        } else {
                            preferences.preferredActivities - activity.title
                        }
                        onPreferencesChanged(preferences.copy(preferredActivities = newActivities))
                    }
                )
            }
        }
    }
}

@Composable
private fun PreferenceCard(
    option: PreferenceOption,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(!isSelected) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = option.title,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = option.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BudgetRangeCard(
    option: PreferenceOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    tint = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column {
                    Text(
                        text = option.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = option.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StepIndicator(
    step: Int,
    currentStep: Int,
    icon: ImageVector,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    when {
                        step == currentStep -> MaterialTheme.colorScheme.primary
                        step < currentStep -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = when {
                    step == currentStep -> MaterialTheme.colorScheme.onPrimary
                    step < currentStep -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = if (step == currentStep)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun isPreferencesValid(preferences: UserPreferences): Boolean {
    return preferences.travelStyle.isNotEmpty() &&
            preferences.budgetRange.isNotEmpty() &&
            preferences.preferredActivities.isNotEmpty()
}