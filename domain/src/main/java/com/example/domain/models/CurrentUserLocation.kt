package com.example.domain.models

import java.util.Locale

data class CurrentUserLocation(
    val latitude: Double,
    val longitude: Double,
    val timeZoneID:String,
    val city: String

) {
    companion object {
        @JvmStatic
        val DEFAULT = CurrentUserLocation(0.0, 0.0, "","")
    }
}
