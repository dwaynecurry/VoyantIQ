package com.voyantiq.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyantiq.app.ai.model.TravelPlanRequest
import com.voyantiq.app.ai.model.TravelPlanResponse
import com.voyantiq.app.ai.service.TravelAIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelAIViewModel @Inject constructor(
    private val travelAIService: TravelAIService
) : ViewModel() {

    private val _uiState = MutableStateFlow<TravelAIUiState>(TravelAIUiState.Initial)
    val uiState: StateFlow<TravelAIUiState> = _uiState

    fun processUserPrompt(prompt: String) {
        _uiState.value = TravelAIUiState.Loading

        viewModelScope.launch {
            try {
                val request = parseUserPrompt(prompt)
                val response = travelAIService.generateTravelPlan(request)
                _uiState.value = TravelAIUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = TravelAIUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun refineItinerary(feedback: String) {
        _uiState.value = TravelAIUiState.Loading

        viewModelScope.launch {
            try {
                val refinedPlan = travelAIService.refineItinerary(feedback)
                _uiState.value = TravelAIUiState.Success(refinedPlan)
            } catch (e: Exception) {
                _uiState.value = TravelAIUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun parseUserPrompt(prompt: String): TravelPlanRequest {
        // Simple parsing logic - in real app, use NLP or more sophisticated parsing
        val destination = extractDestination(prompt)
        val budget = extractBudget(prompt)
        val duration = extractDuration(prompt)
        val activities = extractActivities(prompt)
        val preferences = extractPreferences(prompt)

        return TravelPlanRequest(
            destination = destination,
            budget = budget,
            duration = duration,
            activities = activities,
            preferences = preferences
        )
    }

    // Helper functions for parsing...
}

sealed class TravelAIUiState {
    object Initial : TravelAIUiState()
    object Loading : TravelAIUiState()
    data class Success(val plan: TravelPlanResponse) : TravelAIUiState()
    data class Error(val message: String) : TravelAIUiState()
}