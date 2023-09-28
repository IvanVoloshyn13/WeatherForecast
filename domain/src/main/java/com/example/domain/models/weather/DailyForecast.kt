package com.example.domain.models.weather

data class DailyForecast(
    val weatherType: WeatherType,
    val maxTemperature: Int,
    val minTemperature: Int,
    val time:String
)
