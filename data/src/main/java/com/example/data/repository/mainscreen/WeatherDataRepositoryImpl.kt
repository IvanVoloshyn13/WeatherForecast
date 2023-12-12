package com.example.data.repository.mainscreen

import com.example.data.di.IoDispatcher
import com.example.data.mappers.toWeatherComponents
import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.repository.main.WeatherDataRepository
import com.example.domain.utils.Resource
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.mainscreen.ApiWeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WeatherDataRepository {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherComponents> = withContext(dispatcher) {

        val networkResult = executeApiCall({
            apiWeatherService.getWeatherData(
                latitude = latitude,
                longitude = longitude
            )
        })

        when (networkResult) {
            is ApiResult.Success -> {
                return@withContext Resource.Success(networkResult.data.toWeatherComponents())
            }

            is ApiResult.Error -> {
                return@withContext Resource.Error( networkResult.message)
            }
        }

    }


}