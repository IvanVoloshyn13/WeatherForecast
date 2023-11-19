package com.example.weatherforecast

import com.example.weatherforecast.utils.GpsStatus
import kotlinx.coroutines.flow.MutableSharedFlow

interface UpdateGpsStatus {
    val gpsStatusFlow: MutableSharedFlow<GpsStatus>
}