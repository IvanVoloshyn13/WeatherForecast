package com.example.domain.mainscreen

import com.example.domain.repository.UserLocationTimezone
import javax.inject.Inject

class GetCurrentUserLocationTimeZoneUseCase @Inject constructor(private val userLocationTimezone: UserLocationTimezone) {
    suspend fun invoke(latitude: Double, longitude: Double): String {
        return userLocationTimezone.getTimeZone(latitude, longitude)
    }
}