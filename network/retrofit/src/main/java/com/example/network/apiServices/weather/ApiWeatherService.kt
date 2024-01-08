package com.example.network.apiServices.weather

import com.example.network.models.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiWeatherService {

    @GET(ENDPOINT)
    suspend fun fetchWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<WeatherResponse>

    companion object {
        const val ENDPOINT = "v1/forecast?&hourly=temperature_2m," +
                "weathercode&daily=weathercode" + ",temperature_2m_max,temperature_2m_min," +
                "sunrise,sunset,uv_index_max&timezone=auto"
    }
}