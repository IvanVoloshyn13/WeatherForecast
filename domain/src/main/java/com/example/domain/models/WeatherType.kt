package com.example.domain.models

sealed class WeatherType(
    val weatherType: String
) {
    object ClearSky : WeatherType(
        weatherType = "Clear sky"
    )

    object MainlyClear : WeatherType(
        weatherType = "Mainly Clear"
    )

    object PartlyCloud : WeatherType(
        weatherType = "Partly Cloud"
    )

    object Overcast : WeatherType(
        weatherType = "Overcast"
    )

    object Foggy : WeatherType(
        weatherType = "Foggy"

    )

    object DepositingRimeFog : WeatherType(
        weatherType = "Depositing rime fog"

    )

    object LightDrizzle : WeatherType(
        weatherType = "Light drizzle"
    )

    object ModerateDrizzle : WeatherType(
        weatherType = "Moderate drizzle"
    )

    object DenseDrizzle : WeatherType(
        weatherType = "Dense drizzle"
    )

    object LightFreezingDrizzle : WeatherType(
        weatherType = "Slight freezing drizzle"
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherType = "Dense freezing drizzle"
    )

    object SlightRain : WeatherType(
        weatherType = "Slight rain"
    )

    object ModerateRain : WeatherType(
        weatherType = "Rainy"
    )

    object HeavyRain : WeatherType(
        weatherType = "Heavy rain"
    )

    object HeavyFreezingRain : WeatherType(
        weatherType = "Heavy freezing rain"
    )

    object SlightSnowFall : WeatherType(
        weatherType = "Slight snow fall"
    )

    object ModerateSnowFall : WeatherType(
        weatherType = "Moderate snow fall"
    )

    object HeavySnowFall : WeatherType(
        weatherType = "Heavy snow fall"
    )

    object SnowGrains : WeatherType(
        weatherType = "Snow grains"
    )

    object SlightRainShowers : WeatherType(
        weatherType = "Slight rain showers"
    )

    object ModerateRainShowers : WeatherType(
        weatherType = "Moderate rain showers"
    )

    object ViolentRainShowers : WeatherType(
        weatherType = "Violent rain showers"
    )

    object SlightSnowShowers : WeatherType(
        weatherType = "Light snow showers"
    )

    object HeavySnowShowers : WeatherType(
        weatherType = "Heavy snow showers"
    )

    object ModerateThunderstorm : WeatherType(
        weatherType = "Moderate thunderstorm"
    )

    object SlightHailThunderstorm : WeatherType(
        weatherType = "Thunderstorm with slight hail"
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherType = "Thunderstorm with heavy hail"
    )


    companion object {
        fun fromWHO(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloud
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }

        }
    }
}



