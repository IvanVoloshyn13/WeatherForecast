package com.example.weatherforecast.screens.main.models


import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo

data class MainScreenState(
    val location: String,
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: ArrayList<HourlyForecast>? = null,
    val dailyForecast: List<DailyForecast>? = null,
    val time: String,

//    val networkStatus: NetworkObserver.NetworkStatus


) {
    companion object {
        val Default = MainScreenState(
            location = "",
            mainWeatherInfo = MainWeatherInfo.Default,
            time = "",

//            networkStatus = NetworkObserver.NetworkStatus.Available
        )
    }
}



