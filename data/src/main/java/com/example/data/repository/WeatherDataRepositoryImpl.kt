package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.mappers.toDailyForecast
import com.example.data.mappers.toHourlyForecast
import com.example.domain.models.weather.WeatherData
import com.example.domain.repository.WeatherDataRepository
import com.example.domain.utils.Resource
import com.example.http.utils.executeApiCall
import com.example.network.APIWeatherService
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val apiWeatherService: APIWeatherService
) : WeatherDataRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherData> {

        val networkResult = executeApiCall({
            apiWeatherService.getWeatherData(
                latitude = latitude,
                longitude = longitude
            )

        })

        val hourlyForecast = networkResult.toHourlyForecast()
        val dailyForecast = networkResult.toDailyForecast()

        return if(networkResult!=null) Resource.Success(
            WeatherData(
                hourlyForecast = hourlyForecast,
                dailyForecast = dailyForecast
            )
        ) else {
            return Resource.Error(message = "")
        }

    }


}