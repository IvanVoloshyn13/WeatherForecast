package com.example.domain.models.mainscreen.weather

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import com.example.domain.R

sealed class WeatherType(
    val weatherType: String,
    val weatherIcon: Int
) {
    object ClearSky : WeatherType(
        weatherType = "Clear sky",
        weatherIcon = R.drawable.ic_clear_sky
    )

    object MainlyClear : WeatherType(
        weatherType = "Mainly Clear",
        weatherIcon = R.drawable.ic_sunnycloudy
    )

    object PartlyCloud : WeatherType(
        weatherType = "Partly Cloud",
        weatherIcon = R.drawable.ic_sunnycloudy
    )

    object Overcast : WeatherType(
        weatherType = "Overcast",
        weatherIcon = R.drawable.ic_cloudy
    )

    object Foggy : WeatherType(
        weatherType = "Foggy",
        weatherIcon = R.drawable.ic_verycloudy

    )

    object DepositingRimeFog : WeatherType(
        weatherType = "Depositing rime fog",
        weatherIcon = R.drawable.ic_verycloudy

    )

    object LightDrizzle : WeatherType(
        weatherType = "Light drizzle",
        weatherIcon = R.drawable.ic_rainshower
    )

    object ModerateDrizzle : WeatherType(
        weatherType = "Moderate drizzle",
        weatherIcon = R.drawable.ic_rainshower
    )

    object DenseDrizzle : WeatherType(
        weatherType = "Dense drizzle",
        weatherIcon = R.drawable.ic_rainshower
    )

    object LightFreezingDrizzle : WeatherType(
        weatherType = "Slight freezing drizzle",
        weatherIcon = R.drawable.ic_snowyrain
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherType = "Dense freezing drizzle",
        weatherIcon = R.drawable.ic_snowyrain
    )

    object SlightRain : WeatherType(
        weatherType = "Slight rain",
        weatherIcon = R.drawable.ic_rainy
    )

    object ModerateRain : WeatherType(
        weatherType = "Rainy",
        weatherIcon = R.drawable.ic_rainy
    )

    object HeavyRain : WeatherType(
        weatherType = "Heavy rain",
        weatherIcon = R.drawable.ic_rainy
    )

    object HeavyFreezingRain : WeatherType(
        weatherType = "Heavy freezing rain",
        weatherIcon = R.drawable.ic_snowyrain
    )

    object SlightSnowFall : WeatherType(
        weatherType = "Slight snow fall",
        weatherIcon = R.drawable.ic_snowy
    )

    object ModerateSnowFall : WeatherType(
        weatherType = "Moderate snow fall",
        weatherIcon = R.drawable.ic_heavy_snow
    )

    object HeavySnowFall : WeatherType(
        weatherType = "Heavy snow fall",
        weatherIcon = R.drawable.ic_heavy_snow
    )

    object SnowGrains : WeatherType(
        weatherType = "Snow grains",
        weatherIcon = R.drawable.ic_heavy_snow
    )

    object SlightRainShowers : WeatherType(
        weatherType = "Slight rain showers",
        weatherIcon = R.drawable.ic_rainshower
    )

    object ModerateRainShowers : WeatherType(
        weatherType = "Moderate rain showers",
        weatherIcon = R.drawable.ic_rainshower
    )

    object ViolentRainShowers : WeatherType(
        weatherType = "Violent rain showers",
        weatherIcon = R.drawable.ic_rainshower
    )

    object SlightSnowShowers : WeatherType(
        weatherType = "Light snow showers",
        weatherIcon = R.drawable.ic_snowy
    )

    object HeavySnowShowers : WeatherType(
        weatherType = "Heavy snow showers",
        weatherIcon = R.drawable.ic_snowy
    )

    object ModerateThunderstorm : WeatherType(
        weatherType = "Moderate thunderstorm",
        weatherIcon = R.drawable.ic_thunder
    )

    object SlightHailThunderstorm : WeatherType(
        weatherType = "Thunderstorm with slight hail",
        weatherIcon = R.drawable.ic_rainthunder
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherType = "Thunderstorm with heavy hail",
        weatherIcon = R.drawable.ic_rainthunder
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



