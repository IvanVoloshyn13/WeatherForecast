package com.example.domain.models.mainscreen.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: ArrayList<HourlyForecast>,
    val dailyForecast: ArrayList<DailyForecast>,
    val timezone: String
)

