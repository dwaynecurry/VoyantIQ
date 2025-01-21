package com.voyantiq.app.data.repository

import com.voyantiq.app.data.model.Trip
import com.voyantiq.app.data.model.TripActivity
import com.voyantiq.app.data.model.TripStatus
import java.time.LocalDate
import java.util.UUID

class TripRepository {
    // Store trips in memory for now (we'll add database later)
    private val trips = mutableMapOf<String, Trip>()

    suspend fun createTrip(
        destination: String,
        startDate: LocalDate,
        endDate: LocalDate,
        budget: Double
    ): Result<Trip> {
        return try {
            val trip = Trip(
                id = UUID.randomUUID().toString(),
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                budget = budget,
                status = TripStatus.PLANNING,
                progress = 0f,
                spent = 0.0,
                activities = emptyList()
            )
            trips[trip.id] = trip
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTrips(): Result<List<Trip>> {
        return try {
            Result.success(trips.values.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTripById(tripId: String): Result<Trip> {
        return try {
            val trip = trips[tripId] ?: throw Exception("Trip not found")
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addActivity(
        tripId: String,
        activity: TripActivity
    ): Result<Trip> {
        return try {
            val existingTrip = trips[tripId] ?: throw Exception("Trip not found")
            val updatedTrip = existingTrip.copy(
                activities = existingTrip.activities + activity,
                progress = calculateProgress(existingTrip.activities + activity),
                spent = (existingTrip.activities + activity).sumOf { it.cost }
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

    private fun calculateProgress(activities: List<TripActivity>): Float {
        if (activities.isEmpty()) return 0f
        val bookedActivities = activities.count { it.isBooked }
        return bookedActivities.toFloat() / activities.size
    }
}