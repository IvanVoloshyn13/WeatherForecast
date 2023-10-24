package com.example.domain.mainscreen

import com.example.domain.models.weather.WeatherComponents
import com.example.domain.repository.WeatherDataRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(private val weatherDataRepository: WeatherDataRepository) {
    suspend fun invoke(latitude: Double, longitude: Double): Resource<WeatherComponents> {
        return weatherDataRepository.getWeatherData(latitude = latitude, longitude = longitude)
    }
}