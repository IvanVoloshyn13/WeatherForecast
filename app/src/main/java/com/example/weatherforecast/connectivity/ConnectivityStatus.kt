package com.example.weatherforecast.connectivity

fun Boolean.toGpsStatus(): GpsStatus {
    return when (this) {
        true -> GpsStatus.Available
        false -> GpsStatus.Unavailable
    }
}

enum class GpsStatus {
    Available, Unavailable
}
