package com.example.domain.models.mainscreen.weather

data class MainWeatherInfo(
    val maxTemperature: Int = 0,
    val minTemperature: Int = 0,
    val weatherType: WeatherType = WeatherType.fromWHO(1),
    val currentTemperature: Int = 0,
)

