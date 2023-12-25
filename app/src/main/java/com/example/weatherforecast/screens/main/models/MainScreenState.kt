package com.example.weatherforecast.screens.main.models


import com.example.domain.models.mainscreen.unsplash.CityImage
import com.example.domain.models.mainscreen.weather.DailyForecast
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.domain.models.mainscreen.weather.MainWeatherInfo


data class MainScreenState(
    val successState: SuccessState,
    val errorState: MainErrorState
)

data class SuccessState(
    val location: String,
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: ArrayList<HourlyForecast>? = null,
    val dailyForecast: ArrayList<DailyForecast>? = null,
    val time: String,
    val cityImage: CityImage? = null,
    val isLoading: Boolean = false,

    ) {
    companion object {
        val Default = SuccessState(
            location = "",
            mainWeatherInfo = MainWeatherInfo.Default,
            time = ""
        )
    }
}

sealed class ErrorState {

    class GpsProviderError(val message: String = "", val e: Exception? = null) : ErrorState()
    class WeatherDataError(val message: String = "", val e: Exception? = null) : ErrorState()
    class ImageLoadingError(val message: String = "", val e: Exception? = null) : ErrorState()
}

data class MainErrorState(
    val gpsProviderError: ErrorState.GpsProviderError? = null,
    val weatherDataError: ErrorState.WeatherDataError? = null,
    val imageLoadingError: ErrorState.ImageLoadingError? = null
)



