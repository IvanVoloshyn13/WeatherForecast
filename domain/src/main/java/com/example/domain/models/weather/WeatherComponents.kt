package com.example.domain.models.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast:  ArrayList<HourlyForecast>,
    val dailyForecast: ArrayList<DailyForecast>
)

