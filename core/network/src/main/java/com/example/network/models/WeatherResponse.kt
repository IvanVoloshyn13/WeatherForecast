package com.example.network.models

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "daily")
    val dailyWeatherData: Daily,
    @Json(name = "hourly")
    val hourlyWeatherData: Hourly,
)