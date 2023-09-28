package com.example.domain.repository

import com.example.domain.models.weather.WeatherComponents
import com.example.domain.utils.Resource

interface WeatherDataRepository {
    suspend fun getWeatherData(latitude: Double, longitude: Double): Resource<WeatherComponents>
}