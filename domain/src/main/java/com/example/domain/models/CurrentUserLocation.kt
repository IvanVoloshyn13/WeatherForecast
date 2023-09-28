package com.example.domain.models

import com.example.domain.R

data class CurrentUserLocation(
    val latitude: Double,
    val longitude: Double,
    val city: String

) {
    companion object {
        @JvmStatic
        val DEFAULT = CurrentUserLocation(0.0, 0.0, "")
    }
}
