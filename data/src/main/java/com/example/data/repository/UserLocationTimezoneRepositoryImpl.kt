package com.example.data.repository

import android.util.Log
import com.example.domain.repository.UserLocationTimezone
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.ApiGoogleTimezoneService
import javax.inject.Inject

class UserLocationTimezoneRepositoryImpl @Inject constructor(
    private val googleTimezoneService: ApiGoogleTimezoneService
) : UserLocationTimezone {
    override suspend fun getTimeZone(latitude: Double, longitude: Double): String {
        val timeStamp = System.currentTimeMillis().toInt()
        val location = "$latitude,$longitude"
        val body =
            executeApiCall({
                googleTimezoneService.getTimeZone(
                    location,
                    timeStamp
                )
            })
        Log.d("TIMEZONE", body!!.timeZoneId)
        return body?.timeZoneId ?: ""
    }
}