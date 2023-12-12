package com.example.network.apiServices.mainscreen

import com.example.network.models.google.GoogleTimezone
import com.example.network.utils.Google
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiGoogleTimezoneService {
    @GET("timezone/json?")
    suspend fun getTimeZone(
        @Query("location") location:String,
        @Query("timestamp") timestamp: Int,
        @Query("key") apiKey: String = Google.MAPS_API_KEY
    ): Response<GoogleTimezone>
}

