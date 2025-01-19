package com.voyantiq.app.ai.model

data class TravelPlanRequest(
    val destination: String,
    val budget: Double,
    val duration: Int,
    val activities: List<String>,
    val preferences: String
)

data class TravelPlanResponse(
    val dailyItineraries: List<DayPlan>,
    val budgetBreakdown: BudgetBreakdown,
    val recommendations: List<Recommendation>,
    val localTips: List<String>,
    val bookingSuggestions: List<BookingSuggestion>
)

data class DayPlan(
    val day: Int,
    val activities: List<PlannedActivity>,
    val totalCost: Double
)

data class PlannedActivity(
    val time: String,
    val name: String,
    val description: String,
    val location: String,
    val cost: Double,
    val category: ActivityCategory,
    val bookingInfo: BookingInfo?
)

data class BudgetBreakdown(
    val accommodation: Double,
    val activities: Double,
    val food: Double,
    val transportation: Double,
    val miscellaneous: Double
)

data class Recommendation(
    val name: String,
    val description: String,
    val category: ActivityCategory,
    val estimatedCost: Double,
    val bestTimeToVisit: String,
    val bookingInfo: BookingInfo?
)

data class BookingSuggestion(
    val type: BookingType,
    val name: String,
    val provider: String,
    val estimatedCost: Double,
    val url: String,
    val notes: String
)

data class BookingInfo(
    val provider: String,
    val url: String,
    val price: Double,
    val availability: Boolean
)

enum class ActivityCategory {
    SIGHTSEEING,
    DINING,
    ADVENTURE,
    RELAXATION,
    CULTURE,
    SHOPPING,
    ENTERTAINMENT
}

enum class BookingType {
    HOTEL,
    FLIGHT,
    ACTIVITY,
    RESTAURANT,
    TRANSPORT
}