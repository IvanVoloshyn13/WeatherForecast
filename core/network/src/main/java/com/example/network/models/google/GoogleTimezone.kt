package com.example.network.models.google

data class GoogleTimezone(
    val dstOffset: Int,
    val rawOffset: Int,
    val status: String,
    val timeZoneId: String,
    val timeZoneName: String
)