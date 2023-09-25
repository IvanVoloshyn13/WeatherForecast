package com.example.domain.models.weather

data class WeatherData(
    val hourlyForecast: Map<Int, List<HourlyForecast>>,
    val dailyForecast: Map<Int, List<DailyForecast>>
)
