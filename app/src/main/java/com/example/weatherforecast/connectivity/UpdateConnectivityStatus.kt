package com.example.weatherforecast.connectivity

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface UpdateConnectivityStatus {
    val gpsStatusFlow: MutableSharedFlow<GpsStatus>
    val networkStatusFlow: MutableSharedFlow<NetworkStatus>
    val networkStatus: MutableStateFlow<ConnectivityStatus>
}