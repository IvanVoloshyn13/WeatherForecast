package com.example.domain.usecase.mainscreen

import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.repository.main.WeatherDataRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(private val weatherDataRepository: WeatherDataRepository) {
    suspend fun invoke(latitude: Double, longitude: Double): Resource<WeatherComponents> {
        return weatherDataRepository.getWeatherData(latitude = latitude, longitude = longitude)
    }
}