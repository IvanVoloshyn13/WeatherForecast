package com.example.data.mappers

import com.example.domain.models.HourlyWeatherData

import com.example.network.models.WeatherResponse


fun WeatherResponse.toHourlyWeatherData(): Map<String, List<HourlyWeatherData>> {

}