package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voyantiq.app.ui.viewmodel.AIPlannerViewModel

@Composable
fun AIPlannerScreen(
    onBackClick: () -> Unit,
    viewModel: AIPlannerViewModel = hiltViewModel()
) {
    var userPrompt by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val planningResult by viewModel.planningResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // AI Input Section
        OutlinedTextField(
            value = userPrompt,
            onValueChange = { userPrompt = it },
            label = { Text("Describe your dream vacation") },
            placeholder = { Text("E.g., Beach vacation in Orlando for $1800, want to visit theme parks and relax") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isGenerating = true
                viewModel.generatePlan(userPrompt)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isGenerating
        ) {
            Text(if (isGenerating) "Generating Plan..." else "Create AI Travel Plan")
        }

        // Results Section
        planningResult?.let { result ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Daily Itineraries
                items(result.dailyItineraries) { dayPlan ->
                    DayPlanCard(dayPlan = dayPlan)
                }

                // Budget Breakdown
                item {
                    BudgetBreakdownCard(breakdown = result.budgetBreakdown)
                }

                // Recommendations
                items(result.recommendations) { recommendation ->
                    RecommendationCard(recommendation = recommendation)
                }

                // Local Tips
                item {
                    LocalTipsCard(tips = result.localTips)
                }
            }
        }
    }
}

// Add supporting composables for cards...