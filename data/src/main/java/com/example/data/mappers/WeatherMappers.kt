package com.example.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.models.HourlyWeatherData
import com.example.domain.models.WeatherType

import com.example.network.models.WeatherResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedHourlyWeatherData(
    val index:Int,
    val data:HourlyWeatherData
)


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toHourlyWeatherData(): Map<Int, List<HourlyWeatherData>> {
    return hourlyWeatherData.time.mapIndexed { index, time ->
        val currentTemp = hourlyWeatherData.temperature_2m[index]
        val weatherType = WeatherType.fromWHO(hourlyWeatherData.weathercode[index])
        IndexedHourlyWeatherData(
            index=index,
            data=HourlyWeatherData(
            currentHour = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            currentTemp = currentTemp.toInt(),
            weatherType = weatherType
            )
        )
    }.groupBy {
        it.index/24
    }.mapValues {
        it.value.map { it.data }
    }

}