package com.example.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val daily: Daily,
    val hourly: Hourly,
)