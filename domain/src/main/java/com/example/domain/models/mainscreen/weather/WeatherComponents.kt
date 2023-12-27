package com.example.domain.models.mainscreen.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: ArrayList<HourlyForecast> = ArrayList(),
    val dailyForecast: ArrayList<DailyForecast> = ArrayList(),
    val timezone: String? = "",

)

