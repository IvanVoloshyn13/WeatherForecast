package com.example.weatherforecast.connectivity

import com.example.weatherforecast.GpsStatus

data class ConnectivityStatus(
    val networkStatus: NetworkStatus?,
    val gpsStatus: GpsStatus?


)
