package com.example.domain.models.weather

import java.time.LocalDateTime


data class HourlyForecast(
    val currentDate: LocalDateTime,
    val currentTemp: Int,
    val weatherType: WeatherType
)
