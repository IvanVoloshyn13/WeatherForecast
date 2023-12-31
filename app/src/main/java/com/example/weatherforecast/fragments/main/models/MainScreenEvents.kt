package com.example.weatherforecast.fragments.main.models

import com.example.domain.models.searchscreen.SearchedCity

sealed class MainScreenEvents
object GetWeatherByCurrentLocation : MainScreenEvents()
object GetSavedLocationsList : MainScreenEvents()
class GetWeather(val city: SearchedCity) : MainScreenEvents()
