package com.example.weatherforecast.screens.main.models

sealed class MainScreenEvents {
    object GetWeatherByCurrentLocation : MainScreenEvents()
    object CheckNetworkConnection:MainScreenEvents()
}