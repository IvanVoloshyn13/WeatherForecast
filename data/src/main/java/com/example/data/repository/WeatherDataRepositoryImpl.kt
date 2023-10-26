package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.mappers.toWeatherComponents
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.repository.WeatherDataRepository
import com.example.domain.utils.Resource
import com.example.http.exeptions.ApiException
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.APIWeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val apiWeatherService: APIWeatherService
) : WeatherDataRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherComponents> = withContext(Dispatchers.IO) {

        val networkResult = executeApiCall({
            apiWeatherService.getWeatherData(
                latitude = latitude,
                longitude = longitude
            )
        })

        return@withContext if (networkResult != null) {
            val weatherComponents = networkResult.toWeatherComponents()
            Resource.Success(
                weatherComponents
            )
        } else {

            return@withContext Resource.Error(message = TODO())
        }

    }


}