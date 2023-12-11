package com.example.data.repository

import com.example.data.di.IoDispatcher
import com.example.domain.repository.UserLocationTimezone
import com.example.domain.utils.Resource
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.ApiGoogleTimezoneService
import com.example.network.utils.GoogleMapsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocationTimezoneRepositoryImpl @Inject constructor(
    @GoogleMapsApi private val googleTimezoneService: ApiGoogleTimezoneService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserLocationTimezone {


    override suspend fun getTimeZone(latitude: Double, longitude: Double): Resource<String> =
        withContext(dispatcher) {
            val timeStamp = System.currentTimeMillis().toInt()
            val location = "$latitude,$longitude"
            val body = executeApiCall({
                googleTimezoneService.getTimeZone(
                    location,
                    timeStamp
                )
            })
            when (body) {
                is ApiResult.Success -> {
                    return@withContext Resource.Success(body.data.timeZoneId)
                }

                is ApiResult.Error -> {
                    return@withContext Resource.Error(data = null, message = body.message)
                }
            }
        }


}