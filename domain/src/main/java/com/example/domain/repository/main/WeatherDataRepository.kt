package com.example.domain.repository.main

import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.utils.Resource

interface WeatherDataRepository {
    suspend fun getWeatherData(latitude: Double, longitude: Double): Resource<WeatherComponents>
}