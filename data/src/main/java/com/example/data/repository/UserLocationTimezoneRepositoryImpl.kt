package com.example.data.repository

import com.example.domain.repository.UserLocationTimezone
import com.example.domain.utils.Resource
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.ApiGoogleTimezoneService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocationTimezoneRepositoryImpl @Inject constructor(
    private val googleTimezoneService: ApiGoogleTimezoneService
) : UserLocationTimezone {


    override suspend fun getTimeZone(latitude: Double, longitude: Double): Resource<String> =
        withContext(Dispatchers.IO) {
            val timeStamp = System.currentTimeMillis().toInt()
            val location = "$latitude,$longitude"
            val body = executeApiCall({
                googleTimezoneService.getTimeZone(
                    location,
                    timeStamp
                )
            })
            return@withContext if (body != null) {
                Resource.Success(body.timeZoneId)
            } else {

                return@withContext Resource.Error(message = TODO())
            }
        }


}