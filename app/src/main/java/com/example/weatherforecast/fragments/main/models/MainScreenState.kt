package com.example.weatherforecast.fragments.main.models


import com.example.domain.models.mainscreen.unsplash.ImageUrl
import com.example.domain.models.mainscreen.weather.DailyForecast
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.domain.models.mainscreen.weather.MainWeatherInfo
import com.example.domain.models.searchscreen.SearchedCity


data class MainScreenState(
    val location: String = "",
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: ArrayList<HourlyForecast>? = ArrayList(),
    val dailyForecast: ArrayList<DailyForecast>? = ArrayList(),
    val time: String = "",
    val currentWeatherLocationImage: ImageUrl = "",
    val cities: ArrayList<SearchedCity> = ArrayList(),
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





