package com.example.domain.models.mainscreen.location

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
