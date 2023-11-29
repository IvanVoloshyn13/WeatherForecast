package com.example.weatherforecast.connectivity

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface UpdateConnectivityStatus {

    val networkStatus: MutableStateFlow<ConnectivityStatus>
}