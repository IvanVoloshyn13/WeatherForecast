package com.example.weatherforecast.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn


fun Context.registerReceiverAsFlow(
    context: Context,
    intentFilter: IntentFilter,
    scope: CoroutineScope
): Flow<GpsStatus> = callbackFlow<GpsStatus> {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            trySend(isEnabled.toGpsStatus())
        }
    }
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, intentFilter, RECEIVER_NOT_EXPORTED)
    } else {
        registerReceiver(receiver, intentFilter)
    }
    awaitClose {
        unregisterReceiver(receiver)
    }
}.distinctUntilChanged()
    .shareIn(started = SharingStarted.WhileSubscribed(), scope = scope)


fun Boolean.toGpsStatus(): GpsStatus {
    return when (this) {
        true -> GpsStatus.Available
        false -> GpsStatus.Unavailable
    }
}

enum class GpsStatus {
    Available, Unavailable
}