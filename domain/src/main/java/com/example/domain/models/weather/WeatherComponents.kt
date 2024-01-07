package com.example.domain.models.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: ArrayList<HourlyForecast> = ArrayList(),
    val dailyForecast: ArrayList<DailyForecast> = ArrayList(),
    val timezone: String? = "",

    )

