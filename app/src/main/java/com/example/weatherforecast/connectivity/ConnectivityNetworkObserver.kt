package com.example.weatherforecast.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityNetworkObserver @Inject constructor(
    @ApplicationContext context: Context
)  {


    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    fun observe(): Flow<NetworkStatus> {
        val networkStatus =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return callbackFlow {
            launch {
                if (networkStatus == null) {
                    trySend(NetworkStatus.Lost)
                }
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        trySend(NetworkStatus.Available)
                    }
                }
                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        trySend(NetworkStatus.Lost)
                    }
                }


            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }

        }.distinctUntilChanged()

    }
}

enum class NetworkStatus {
    Available,  Lost
}