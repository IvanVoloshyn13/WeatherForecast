package com.example.domain.models.weather

data class DailyForecast(
    val weatherCode: Int,
    val maxTemperature: Int,
    val minTemperature: Int,
    val dayOfTheWeek: String
)
