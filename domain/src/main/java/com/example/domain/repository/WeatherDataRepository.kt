package com.example.domain.repository

import com.example.domain.models.weather.HourlyForecast
import com.example.domain.utils.Resource

interface WeatherDataRepository {
    suspend fun getWeatherData(latitude: Double, longitude: Double): Resource<HourlyForecast>
}