package com.example.weatherforecast.connectivity

import kotlinx.coroutines.flow.MutableSharedFlow

interface UpdateConnectivityStatus {

    val gpsStatus: MutableSharedFlow<GpsStatus>
    val networkStatus: MutableSharedFlow<NetworkStatus>
}