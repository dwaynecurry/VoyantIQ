package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpService {
    @GET("businesses/search")
    suspend fun searchRestaurants(
        @Header("Authorization") apiKey: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 1000,
        @Query("limit") limit: Int = 20
    ): YelpResponse
}

data class YelpResponse(
    val businesses: List<YelpBusiness>
)

data class YelpBusiness(
    val id: String,
    val name: String,
    val rating: Double,
    val price: String?,
    val phone: String,
    val distance: Double,
    val location: YelpLocation,
    val coordinates: YelpCoordinates,
    val photos: List<String>
)

data class YelpLocation(
    val address1: String,
    val city: String,
    val state: String,
    val zip_code: String
)

data class YelpCoordinates(
    val latitude: Double,
    val longitude: Double
)