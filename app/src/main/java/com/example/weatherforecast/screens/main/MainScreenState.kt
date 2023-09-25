package com.example.weatherforecast.screens.main

import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo

data class MainScreenState(
    val city: String,
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: HourlyForecast? = null,
    val dailyForecast: DailyForecast? = null,

    ) {
    companion object {
        val Default = MainScreenState(
            city = "",
            mainWeatherInfo = MainWeatherInfo.Default,

            )
    }
}
