package com.example.weatherforecast.fragments.main.models


import com.example.domain.models.mainscreen.unsplash.ImageUrl
import com.example.domain.models.mainscreen.weather.DailyForecast
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.domain.models.mainscreen.weather.MainWeatherInfo


data class MainScreenState(
    val location: String = "",
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: ArrayList<HourlyForecast>? = ArrayList(),
    val dailyForecast: ArrayList<DailyForecast>? = ArrayList(),
    val time: String = "",
    val currentWeatherLocationImage: ImageUrl = "",
    val gpsProviderError: ErrorState.GpsProviderError? = null,
    val weatherDataError: ErrorState.WeatherDataError? = null,
    val imageLoadingError: ErrorState.ImageLoadingError? = null,
)

sealed class ErrorState {

    class GpsProviderError(val message: String = "", val e: Exception? = null) : ErrorState()
    class WeatherDataError(val message: String = "", val e: Exception? = null) : ErrorState()
    class ImageLoadingError(val message: String = "", val e: Exception? = null) : ErrorState()
}





