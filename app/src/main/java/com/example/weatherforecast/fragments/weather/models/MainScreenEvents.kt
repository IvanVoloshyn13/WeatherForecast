package com.example.weatherforecast.fragments.weather.models

import com.example.domain.models.SearchedCity

sealed class MainScreenEvents
object GetWeatherByCurrentLocation : MainScreenEvents()
object GetSavedLocationsList : MainScreenEvents()
class GetWeather(val city: SearchedCity) : MainScreenEvents()
class GetLocationById(val cityId: Int) : MainScreenEvents()
object ShowMore : MainScreenEvents()
object ShowLess : MainScreenEvents()
