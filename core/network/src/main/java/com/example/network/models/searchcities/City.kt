package com.example.network.models.searchcities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val timezone: String = ""
)