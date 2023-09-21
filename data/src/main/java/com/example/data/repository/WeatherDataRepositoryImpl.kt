package com.example.data.repository

import android.util.Log
import com.example.domain.models.HourlyWeatherData
import com.example.domain.repository.WeatherDataRepository
import com.example.domain.utils.Resource
import com.example.network.APIWeatherService
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val apiWeatherService: APIWeatherService
) : WeatherDataRepository {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Resource<HourlyWeatherData> {
        val networkResult = apiWeatherService.getWeatherData(
            latitude = latitude,
            longitude = longitude
        )
        return if (networkResult.isSuccessful) {
            Log.d(
                "WEATHER",
                networkResult.body()!!.dailyWeatherData.temperature_2m_max[0].toString()
            )
          TODO()
        } else {
            Resource.Error(message = "")

        }

    }


}