package com.example.domain.models.weather

data class MainWeatherInfo(
    val maxTemperature: Int = 0,
    val minTemperature: Int = 0,
    val weatherCode: Int = 1,
    val currentTemperature: Int = 0,
)

