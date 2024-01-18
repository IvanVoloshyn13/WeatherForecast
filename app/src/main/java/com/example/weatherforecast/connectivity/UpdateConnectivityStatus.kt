package com.example.weatherforecast.connectivity

import kotlinx.coroutines.flow.Flow


interface UpdateConnectivityStatus {

    val gpsStatus: Flow<GpsStatus>
    val networkStatus: Flow<NetworkStatus>
}