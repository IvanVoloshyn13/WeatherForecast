package com.example.weatherforecast.screens.main.models


import com.example.domain.models.mainscreen.unsplash.CityImage
import com.example.domain.models.mainscreen.weather.DailyForecast
import com.example.domain.models.mainscreen.weather.HourlyForecast
import com.example.domain.models.mainscreen.weather.MainWeatherInfo

data class MainScreenState(
    val location: String,
    val mainWeatherInfo: MainWeatherInfo,
    val hourlyForecast: ArrayList<HourlyForecast>? = null,
    val dailyForecast: ArrayList<DailyForecast>? = null,
    val time: String,
    val cityImage: CityImage?=null

//    val networkStatus: NetworkObserver.NetworkStatus


) {
    companion object {
        val Default = MainScreenState(
            location = "",
            mainWeatherInfo = MainWeatherInfo.Default,
            time = ""

//            networkStatus = NetworkObserver.NetworkStatus.Available
        )
    }
}



