package com.voyantiq.app.ai.service

import android.content.Context
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.voyantiq.app.R
import com.voyantiq.app.ai.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelAIService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val openAI = OpenAI(apiKey = context.getString(R.string.openai_api_key))
    private val conversationHistory = mutableListOf<ChatMessage>()

    suspend fun generateTravelPlan(request: TravelPlanRequest): TravelPlanResponse {
        val systemPrompt = ChatMessage(
            role = ChatRole.System,
            content = """
                You are an expert travel planner. Create detailed, actionable travel plans.
                Consider: budget constraints, preferred activities, local events, and seasonal factors.
                Format responses in a structured way for easy parsing.
            """.trimIndent()
        )

        val userPrompt = ChatMessage(
            role = ChatRole.User,
            content = """
                Create a travel plan with:
                Destination: ${request.destination}
                Budget: $${request.budget}
                Duration: ${request.duration} days
                Activities: ${request.activities.joinToString(", ")}
                Additional Preferences: ${request.preferences}
                
                Provide:
                1. Daily itinerary
                2. Budget breakdown
                3. Activity recommendations
                4. Local tips
                5. Booking suggestions
            """.trimIndent()
        )

        conversationHistory.add(userPrompt)

        val completion = openAI.chatCompletion(
            ChatCompletionRequest(
                model = ModelId("gpt-4"),
                messages = listOf(systemPrompt) + conversationHistory,
                temperature = 0.7
            )
        )

        val aiResponse = completion.choices.first().message
        conversationHistory.add(aiResponse)

        return parseAIResponse(aiResponse.content)
    }

    suspend fun refineItinerary(feedback: String): TravelPlanResponse {
        val refinementPrompt = ChatMessage(
            role = ChatRole.User,
            content = "Please refine the previous itinerary based on this feedback: $feedback"
        )

        conversationHistory.add(refinementPrompt)

        val completion = openAI.chatCompletion(
            ChatCompletionRequest(
                model = ModelId("gpt-4"),
                messages = conversationHistory,
                temperature = 0.7
            )
        )

        val aiResponse = completion.choices.first().message
        conversationHistory.add(aiResponse)

        return parseAIResponse(aiResponse.content)
    }

    private fun parseAIResponse(response: String): TravelPlanResponse {
        // Implement parsing logic based on the structured response
        // This is a simplified version
        return TravelPlanResponse(
            dailyItineraries = parseDailyItineraries(response),
            budgetBreakdown = parseBudgetBreakdown(response),
            recommendations = parseRecommendations(response),
            localTips = parseLocalTips(response),
            bookingSuggestions = parseBookingSuggestions(response)
        )
    }

    // Parsing helper functions...
}