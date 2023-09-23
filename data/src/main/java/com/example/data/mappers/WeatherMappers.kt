package com.example.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.WeatherType

import com.example.network.models.WeatherResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedHourlyWeatherData(
    val index: Int,
    val data: HourlyForecast
)

private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyForecast
)


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toHourlyForecast(): Map<Int, List<HourlyForecast>> {
    return hourly.time.mapIndexed { index, time ->
        val currentTemp = hourly.temperature_2m[index]
        val weatherType = WeatherType.fromWHO(hourly.weathercode[index])
        IndexedHourlyWeatherData(
            index = index,
            data = HourlyForecast(
                currentHour = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                currentTemp = currentTemp.toInt(),
                weatherType = weatherType
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

    fun WeatherResponse.toDailyForecast(): Map<Int, List<DailyForecast>> {
        return daily.time.mapIndexed { index, time ->
            val maxTemperature = daily.temperature_2m_max[index].toInt()
            val minTemperature = daily.temperature_2m_min[index].toInt()
            val weatherType = WeatherType.fromWHO(daily.weathercode[index])
            IndexedDailyWeatherData(
                index = index,
                data = DailyForecast(
                    weatherType = weatherType,
                    maxTemperature = maxTemperature,
                    minTemperature = minTemperature
                )
            )
        }.groupBy {
            it.index / 7
        }.mapValues {
            it.value.map { it.data }
        }
    }


