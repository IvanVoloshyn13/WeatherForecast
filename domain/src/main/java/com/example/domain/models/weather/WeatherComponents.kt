package com.example.domain.models.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: Map<Int, List<HourlyForecast>>,
    val dailyForecast: Map<Int, List<DailyForecast>>
)

