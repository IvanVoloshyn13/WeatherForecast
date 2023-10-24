package com.example.network.apiServices

import com.example.network.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIWeatherService {

    @GET(
        "v1/forecast?&hourly=temperature_2m," +
                "weathercode&daily=weathercode" + ",temperature_2m_max,temperature_2m_min," +
                "sunrise,sunset,uv_index_max&timezone=auto"
    )
    suspend fun getWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<WeatherResponse>
}