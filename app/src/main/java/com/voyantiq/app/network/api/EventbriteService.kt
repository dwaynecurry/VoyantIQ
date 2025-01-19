package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface EventbriteService {
    @GET("events/search")
    suspend fun searchEvents(
        @Query("location.latitude") latitude: Double,
        @Query("location.longitude") longitude: Double,
        @Query("location.within") radius: String = "10km",
        @Query("start_date.keyword") dateRange: String = "this_week"
    ): EventsResponse
}

data class EventsResponse(
    val events: List<Event>
)

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val start: EventDate,
    val end: EventDate,
    val venue: Venue,
    val ticketClasses: List<TicketClass>
)

data class EventDate(
    val local: String,
    val timezone: String
)

data class Venue(
    val id: String,
    val name: String,
    val address: Address,
    val latitude: Double,
    val longitude: Double
)

data class TicketClass(
    val name: String,
    val cost: Price,
    val availableQuantity: Int
)