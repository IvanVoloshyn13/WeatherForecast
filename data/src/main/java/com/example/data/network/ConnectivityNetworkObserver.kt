package com.example.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.domain.connectivity.NetworkObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityNetworkObserver @Inject constructor(
    @ApplicationContext context: Context
) : NetworkObserver {


    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    override fun observe(): Flow<NetworkObserver.NetworkStatus> {
        val networkStatus =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return callbackFlow {
            launch {
                if (networkStatus == null) {
                    trySend(NetworkObserver.NetworkStatus.Unavailable)
                }
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        trySend(NetworkObserver.NetworkStatus.Available)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        trySend(NetworkObserver.NetworkStatus.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        trySend(NetworkObserver.NetworkStatus.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        trySend(NetworkObserver.NetworkStatus.Unavailable)
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