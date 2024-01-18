package com.example.weatherforecast.fragments.weather.models


import com.example.domain.models.SearchedCity
import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo


data class MainScreenState(
    val isLoading: Boolean = false,
    val location: String = "",
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: ArrayList<HourlyForecast>? = ArrayList(),
    val dailyForecast: ArrayList<DailyForecast>? = ArrayList(),
    val time: String = "",
    val currentWeatherLocationImage: String = "",
    val cities: List<SearchedCity> = ArrayList(),
    val gpsProviderError: ErrorState.GpsProviderError? = null,
    val weatherDataError: ErrorState.WeatherDataError? = null,
    val imageLoadingError: ErrorState.ImageLoadingError? = null,
    val showMoreLess: ShowMoreLess = ShowMoreLess.Hide
)

sealed class ErrorState {

    class GpsProviderError(val message: String = "", val e: Exception? = null) : ErrorState()
    class WeatherDataError(val message: String = "", val e: Exception? = null) : ErrorState()
    class ImageLoadingError(val message: String = "", val e: Exception? = null) : ErrorState()
}

sealed class ShowMoreLess {
    object ShowMore : ShowMoreLess()
    object ShowLess : ShowMoreLess()
    object Hide : ShowMoreLess()
}





