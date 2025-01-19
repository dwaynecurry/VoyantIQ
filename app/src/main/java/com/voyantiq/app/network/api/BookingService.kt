package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BookingService {
    @GET("hotels")
    suspend fun searchHotels(
        @Query("city_id") cityId: String,
        @Query("checkin") checkIn: String,
        @Query("checkout") checkOut: String,
        @Query("guests") guests: Int,
        @Query("rooms") rooms: Int = 1
    ): HotelsResponse
}

data class HotelsResponse(
    val hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val rating: Double,
    val address: String,
    val price: Price,
    val amenities: List<String>,
    val images: List<String>,
    val availableRooms: Int
)