package com.example.weatherforecast.connectivity

data class ConnectivityStatus(
    val networkStatus: NetworkStatus,
    val gpsStatus: GpsStatus


) {
    companion object {
        val DEFAULT = ConnectivityStatus(
            networkStatus = NetworkStatus.Available,
            gpsStatus = GpsStatus.Available
        )
    }
}
