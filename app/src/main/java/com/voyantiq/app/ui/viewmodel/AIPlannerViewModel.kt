package com.voyantiq.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyantiq.app.network.api.TravelAIService
import com.voyantiq.app.network.api.TravelPlanResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIPlannerViewModel @Inject constructor(
    private val travelAIService: TravelAIService
) : ViewModel() {

    private val _planningResult = MutableStateFlow<TravelPlanResponse?>(null)
    val planningResult: StateFlow<TravelPlanResponse?> = _planningResult

    fun generatePlan(prompt: String) {
        viewModelScope.launch {
            try {
                // Parse prompt to extract key information
                val (destination, budget, activities) = parsePrompt(prompt)

                // Generate plan using AI
                val plan = travelAIService.generateTravelPlan(
                    destination = destination,
                    budget = budget,
                    activities = activities,
                    duration = 7 // Default or extracted from prompt
                )

                _planningResult.value = plan
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun parsePrompt(prompt: String): Triple<String, Double, List<String>> {
        // Implement prompt parsing logic
        // Extract destination, budget, and activities from the natural language prompt
        return Triple("", 0.0, emptyList())
    }
}