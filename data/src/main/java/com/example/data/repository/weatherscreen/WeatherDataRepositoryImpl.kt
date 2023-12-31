package com.example.data.repository.weatherscreen

import com.example.data.di.IoDispatcher
import com.example.data.mappers.toResourceError
import com.example.data.mappers.toWeatherComponents
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.repository.weather.WeatherDataRepository
import com.example.domain.utils.Resource
import com.example.http.exeptions.ApiException
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.weather.ApiWeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WeatherDataRepository {
    override suspend fun fetchWeatherData(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherComponents> = withContext(dispatcher) {
        try {
            val networkResult = executeApiCall({
                apiWeatherService.fetchWeatherData(
                    latitude = latitude,
                    longitude = longitude
                )
            })
            when (networkResult) {
                is ApiResult.Success -> {
                    return@withContext Resource.Success(networkResult.data.toWeatherComponents())
                }

                is ApiResult.Error -> {
                    return@withContext Resource.Error(message = networkResult.message)
                }
            }
        } catch (e: ApiException) {
            return@withContext e.toResourceError()
        }
    }
}


