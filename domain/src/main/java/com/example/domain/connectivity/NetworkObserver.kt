package com.example.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    fun observe(): Flow<NetworkStatus>

    enum class NetworkStatus {
        Available, Unavailable, Losing, Lost
    }
}