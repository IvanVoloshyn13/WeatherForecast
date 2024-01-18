package com.example.weatherforecast.fragments.weather.models

import com.example.domain.models.SearchedCity

sealed class MainScreenIntent
object GetWeatherByCurrentLocation : MainScreenIntent()
object GetSavedLocationsList : MainScreenIntent()
class GetWeather(val city: SearchedCity) : MainScreenIntent()
class GetLocationById(val cityId: Int) : MainScreenIntent()
object ShowMoreCities : MainScreenIntent()
object ShowLessCities : MainScreenIntent()
