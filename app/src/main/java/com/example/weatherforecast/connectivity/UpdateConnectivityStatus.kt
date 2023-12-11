package com.example.weatherforecast.connectivity

import com.example.weatherforecast.GpsStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface UpdateConnectivityStatus {

    val gpsStatus: MutableSharedFlow<GpsStatus>
    val networkStatus: MutableSharedFlow<NetworkStatus>
}