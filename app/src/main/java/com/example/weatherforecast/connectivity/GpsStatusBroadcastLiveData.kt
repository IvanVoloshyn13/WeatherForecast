package com.example.weatherforecast.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData

class GpsStatusBroadcastLiveData(private val context: Context) : LiveData<GpsStatus>() {

    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                checkGpsStatus()
            }
        }
    }

    override fun onActive() {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(broadcastReceiver, filter)
        checkGpsStatus()
    }

    fun checkGpsStatus() {
        val status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            .toGpsStatus()
        postValue(status)
    }

    override fun onInactive() {
        context.unregisterReceiver(broadcastReceiver)
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