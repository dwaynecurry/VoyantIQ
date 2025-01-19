package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TicketmasterService {
    @GET("events")
    suspend fun searchEvents(
        @Query("apikey") apiKey: String,
        @Query("latlong") latLong: String,
        @Query("radius") radius: Int = 10,
        @Query("size") size: Int = 20
    ): TicketmasterResponse
}

data class TicketmasterResponse(
    val _embedded: Embedded
)

data class Embedded(
    val events: List<Event>
)

data class Event(
    val id: String,
    val name: String,
    val type: String,
    val url: String,
    val dates: Dates,
    val priceRanges: List<PriceRange>?,
    val images: List<Image>
)

data class Dates(
    val start: Start
)

data class Start(
    val localDate: String,
    val localTime: String
)

data class Image(
    val url: String,
    val ratio: String
)