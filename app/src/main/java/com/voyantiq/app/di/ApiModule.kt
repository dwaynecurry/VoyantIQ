package com.voyantiq.app.di

import android.content.Context
import com.aallam.openai.client.OpenAI
import com.voyantiq.app.network.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAI(@ApplicationContext context: Context): OpenAI {
        return OpenAI(
            token = context.getString(R.string.openai_api_key)
        )
    }

    @Provides
    @Singleton
    fun provideYelpService(okHttpClient: OkHttpClient, @ApplicationContext context: Context): YelpService {
        return Retrofit.Builder()
            .baseUrl("https://api.yelp.com/v3/")
            .client(okHttpClient.newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${context.getString(R.string.yelp_api_key)}")
                        .build()
                    chain.proceed(request)
                }
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpService::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketmasterService(okHttpClient: OkHttpClient, @ApplicationContext context: Context): TicketmasterService {
        return Retrofit.Builder()
            .baseUrl("https://app.ticketmaster.com/discovery/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TicketmasterService::class.java)
    }

    @Provides
    @Singleton
    fun provideExpediaService(okHttpClient: OkHttpClient, @ApplicationContext context: Context): ExpediaService {
        return Retrofit.Builder()
            .baseUrl("https://hotels.expedia.com/api/")
            .client(okHttpClient.newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${context.getString(R.string.expedia_api_key)}")
                        .build()
                    chain.proceed(request)
                }
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExpediaService::class.java)
    }

    @Provides
    @Singleton
    fun provideTravelAIService(
        @ApplicationContext context: Context,
        openAI: OpenAI
    ): TravelAIService {
        return TravelAIService(context, openAI)
    }

    @Provides
    @Singleton
    fun provideUnifiedDiscoveryService(
        yelpService: YelpService,
        ticketmasterService: TicketmasterService,
        expediaService: ExpediaService
    ): UnifiedDiscoveryService {
        return UnifiedDiscoveryService(
            yelpService,
            ticketmasterService,
            expediaService
        )
    }
}