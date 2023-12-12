package com.example.domain.models.mainscreen.weather

data class DailyForecast(
    val weatherType: WeatherType,
    val maxTemperature: Int,
    val minTemperature: Int,
    val dayOfTheWeek: String
)
