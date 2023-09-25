package com.example.domain.usecase.mainscreen

import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.WeatherData
import com.example.domain.repository.WeatherDataRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(private val weatherDataRepository: WeatherDataRepository) {
    suspend fun invoke(latitude: Double, longitude: Double): Resource<WeatherData> {
        return weatherDataRepository.getWeatherData(latitude = latitude, longitude = longitude)
    }
}