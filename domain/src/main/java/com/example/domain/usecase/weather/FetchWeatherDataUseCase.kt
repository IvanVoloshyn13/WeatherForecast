package com.example.domain.usecase.weather

import com.example.domain.models.weather.WeatherComponents
import com.example.domain.repository.weather.WeatherDataRepository
import com.example.domain.utils.Resource

class FetchWeatherDataUseCase(private val weatherDataRepository: WeatherDataRepository) {
    suspend fun invoke(latitude: Double, longitude: Double): Resource<WeatherComponents> {
        return weatherDataRepository.fetchWeatherData(latitude = latitude, longitude = longitude)
    }
}