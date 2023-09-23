package com.example.domain.models.weather

import java.time.LocalDateTime

data class HourlyForecast(
    val currentHour: LocalDateTime,
    val currentTemp: Int,
    val weatherType: WeatherType
)
