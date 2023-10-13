package com.example.weatherforecast.screens.main.models

import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo

data class MainScreenState(
    val city: String,
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: List<HourlyForecast>? = null,
    val dailyForecast: List<DailyForecast>? = null,

    ) {
    companion object {
        val Default = MainScreenState(
            city = "",
            mainWeatherInfo = MainWeatherInfo.Default,

            )
    }
}
