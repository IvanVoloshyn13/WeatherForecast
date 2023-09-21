package com.example.domain.models

import java.time.LocalDateTime

data class HourlyWeatherData(
    val currentHour: LocalDateTime,
    val currentTemp: Int,
    val weatherType: WeatherType
)
