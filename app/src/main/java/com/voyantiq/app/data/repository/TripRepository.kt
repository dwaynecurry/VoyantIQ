package com.voyantiq.app.data.repository

import com.voyantiq.app.data.model.Trip
import com.voyantiq.app.data.model.Activity
import com.voyantiq.app.data.model.TripStatus
import java.time.LocalDate
import java.util.UUID

class TripRepository {
    // Store trips in memory for now (we'll add database later)
    private val trips = mutableMapOf<String, Trip>()

    suspend fun createTrip(
        userId: String,
        destination: String,
        startDate: LocalDate,
        endDate: LocalDate,
        budget: Double
    ): Result<Trip> {
        return try {
            val trip = Trip(
                id = UUID.randomUUID().toString(),
                userId = userId,
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                budget = budget,
                activities = emptyList(),
                status = TripStatus.PLANNED
            )
            trips[trip.id] = trip
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTripsForUser(userId: String): Result<List<Trip>> {
        return try {
            val userTrips = trips.values.filter { it.userId == userId }
            Result.success(userTrips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addActivity(
        tripId: String,
        activity: Activity
    ): Result<Trip> {
        return try {
            val existingTrip = trips[tripId] ?: throw Exception("Trip not found")
            val updatedTrip = existingTrip.copy(
                activities = existingTrip.activities + activity
            )
            trips[tripId] = updatedTrip
            Result.success(updatedTrip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTripStatus(
        tripId: String,
        status: TripStatus
    ): Result<Trip> {
        return try {
            val existingTrip = trips[tripId] ?: throw Exception("Trip not found")
            val updatedTrip = existingTrip.copy(status = status)
            trips[tripId] = updatedTrip
            Result.success(updatedTrip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}