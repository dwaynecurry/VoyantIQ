package com.voyantiq.app.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GrouponService {
    @GET("deals")
    suspend fun getDeals(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): DealsResponse
}

data class DealsResponse(
    val deals: List<Deal>
)

data class Deal(
    val id: String,
    val title: String,
    val shortAnnouncementTitle: String,
    val finePrint: String,
    val price: Price,
    val merchant: Merchant,
    val options: List<Option>,
    val imageUrl: String
)

data class Price(
    val amount: Double,
    val formattedAmount: String,
    val regularAmount: Double,
    val discountPercent: Int
)

data class Merchant(
    val name: String,
    val websiteUrl: String?,
    val ratings: Int,
    val reviewsCount: Int
)

data class Option(
    val id: String,
    val title: String,
    val price: Price
)