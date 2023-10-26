package com.example.domain.repository

import com.example.domain.utils.Resource

interface UserLocationTimezone {
    suspend fun getTimeZone(latitude: Double, longitude: Double): Resource<String>
}