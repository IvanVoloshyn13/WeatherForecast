package com.example.domain.models.weather

data class MainWeatherInfo(
    val maxTemperature: Int,
    val minTemperature: Int,
    val weatherType: WeatherType,
    val currentTemperature: Int
) {
    companion object {
        val Default = MainWeatherInfo(
            maxTemperature = 0,
            minTemperature = 0,
            weatherType = WeatherType.fromWHO(1),
            currentTemperature = 0
        )
    }
}
