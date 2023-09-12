package com.example.data.utils

class ProvidersError(
    val isPermissionGranted: Boolean,
    val isNetworkEnabled: Boolean,
    val isGpsEnabled: Boolean
) {

    fun allProvidersError(): String {

    }

    fun permissionError(): String {

    }

    fun gpsError(): String {

    }

    fun networkError(): String {

    }

}