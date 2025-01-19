package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExpediaService {
    @GET("properties/search")
    suspend fun searchHotels(
        @Header("Authorization") apiKey: String,
        @Query("location") location: String,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String,
        @Query("adults") adults: Int = 2
    ): ExpediaResponse
}

data class ExpediaResponse(
    val hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val rating: Double,
    val price: Price,
    val amenities: List<String>,
    val images: List<String>,
    val address: Address
)

data class Price(
    val current: Double,
    val original: Double?,
    val currency: String
)

data class Address(
    val line1: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val countryCode: String
)