package com.voyantiq.app.data.model

import java.time.LocalDate

data class Trip(
    val id: String,
    val userId: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double,
    val activities: List<Activity> = emptyList(),
    val status: TripStatus = TripStatus.PLANNED
)

data class Activity(
    val id: String,
    val name: String,
    val description: String,
    val date: LocalDate,
    val time: String,
    val cost: Double,
    val location: Location,
    val type: ActivityType
)

data class Location(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

enum class ActivityType {
    SIGHTSEEING,
    DINING,
    ENTERTAINMENT,
    TRANSPORTATION,
    ACCOMMODATION
}

enum class TripStatus {
    PLANNED,
    ONGOING,
    COMPLETED,
    CANCELLED
}