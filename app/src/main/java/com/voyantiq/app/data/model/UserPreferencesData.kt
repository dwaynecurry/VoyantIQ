package com.voyantiq.app.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class UserPreferences(
    val travelStyle: List<String> = emptyList(),
    val budgetRange: String = "",
    val preferredActivities: List<String> = emptyList(),
    val foodPreferences: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList()
)

data class PreferenceOption(
    val title: String,
    val icon: ImageVector,
    val description: String
)

data class PreferenceStep(
    val title: String,
    val icon: ImageVector,
    val content: @Composable (UserPreferences) -> Unit
)