package com.example.domain.repository.weather

import com.example.domain.models.weather.WeatherComponents
import com.example.domain.utils.Resource

interface WeatherDataRepository {
    suspend fun fetchWeatherData(latitude: Double, longitude: Double): Resource<WeatherComponents>
}