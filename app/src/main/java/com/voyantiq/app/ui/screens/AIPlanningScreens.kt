package com.voyantiq.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voyantiq.app.ui.viewmodel.TravelAIViewModel
import com.voyantiq.app.ui.viewmodel.TravelAIUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIPlannerScreen(
    onBackClick: () -> Unit,
    viewModel: TravelAIViewModel = hiltViewModel()
) {
    var userPrompt by remember { mutableStateOf("") }
    var showRefinementDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Travel Planner") },
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
        ) {
            // User Input Section
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                label = { Text("Describe your dream vacation") },
                placeholder = {
                    Text("E.g., Beach vacation in Orlando for $1800, want to visit theme parks")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Button(
                onClick = { viewModel.processUserPrompt(userPrompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Generate Travel Plan")
            }

            // Results Section
            when (val state = uiState) {
                is TravelAIUiState.Loading -> {
                    LoadingIndicator()
                }
                is TravelAIUiState.Success -> {
                    TravelPlanResults(
                        plan = state.plan,
                        onRefineClick = { showRefinementDialog = true }
                    )
                }
                is TravelAIUiState.Error -> {
                    ErrorMessage(message = state.message)
                }
                else -> Unit
            }
        }

        if (showRefinementDialog) {
            RefinementDialog(
                onDismiss = { showRefinementDialog = false },
                onRefine = { feedback ->
                    viewModel.refineItinerary(feedback)
                    showRefinementDialog = false
                }
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TravelPlanResults(
    plan: TravelPlanResponse,
    onRefineClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Daily Itineraries
        items(plan.dailyItineraries) { dayPlan ->
            DayPlanCard(dayPlan = dayPlan)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Budget Breakdown
        item {
            BudgetBreakdownCard(breakdown = plan.budgetBreakdown)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Recommendations
        items(plan.recommendations) { recommendation ->
            RecommendationCard(recommendation = recommendation)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Local Tips
        item {
            LocalTipsCard(tips = plan.localTips)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Booking Suggestions
        items(plan.bookingSuggestions) { suggestion ->
            BookingSuggestionCard(suggestion = suggestion)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Refine Button
        item {
            Button(
                onClick = onRefineClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Refine This Plan")
            }
        }
    }
}

// Add other composable components (cards, dialogs, etc.)...