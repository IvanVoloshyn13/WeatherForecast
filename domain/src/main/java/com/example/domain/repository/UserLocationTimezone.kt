package com.example.domain.repository

interface UserLocationTimezone {
    suspend fun getTimeZone(latitude: Double, longitude: Double): String
}