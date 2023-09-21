package com.example.domain.models

data class HourlyWeatherData(
    val currentHour: String,
    val currentTemp: Int,
    val weatherCode: WeatherType
)
