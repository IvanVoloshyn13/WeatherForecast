package com.example.weatherforecast.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager


class GpsStatusBroadcastReceiver(
    val listener: GpsStatusListener,
    val context: Context
) : BroadcastReceiver() {


    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                .toGpsStatus()
            listener.receiveGpsStatus(status)

        }
    }

    interface GpsStatusListener {
        fun receiveGpsStatus(gpsStatus: GpsStatus)
    }
}


fun Boolean.toGpsStatus(): GpsStatus {
    return when (this) {
        true -> GpsStatus.Available
        false -> GpsStatus.Unavailable
    }
}

enum class GpsStatus {
    Available, Unavailable
}

