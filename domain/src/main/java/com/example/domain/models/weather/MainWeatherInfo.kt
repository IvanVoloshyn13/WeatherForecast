package com.example.domain.models.weather

import com.example.domain.R

data class MainWeatherInfo(
    val maxTemperature: Int,
    val minTemperature: Int,
    val weatherType: WeatherType,
    val currentTemperature: Int,
    val currentTime: String,
) {
    companion object {
        val Default = MainWeatherInfo(
            maxTemperature = 0,
            minTemperature = 0,
            weatherType = WeatherType.fromWHO(1),
            currentTemperature = 0,
            currentTime = "0:00",
        )
    }
}
