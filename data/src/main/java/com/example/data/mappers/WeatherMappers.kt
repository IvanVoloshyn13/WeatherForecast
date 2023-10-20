package com.example.data.mappers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.models.weather.WeatherType

import com.example.network.models.WeatherResponse
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
                currentDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                currentTemp = currentTemp.toInt(),
                weatherType = weatherType
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { hourlyWeatherData -> hourlyWeatherData.data }
    }
}

fun WeatherResponse.toDailyForecast(): Map<Int, List<DailyForecast>> {
    return List(daily.time.size) { index ->
        val maxTemperature = daily.temperature_2m_max[index].toInt()
        val minTemperature = daily.temperature_2m_min[index].toInt()
        val dayTime = daily.time[index]
        val weatherType = WeatherType.fromWHO(daily.weathercode[index])
        IndexedDailyWeatherData(
            index = index,
            data = DailyForecast(
                weatherType = weatherType,
                maxTemperature = maxTemperature,
                minTemperature = minTemperature,
                time = dayTime
            )
        )
    }.groupBy {
        it.index
    }.mapValues {
        it.value.map { dailyWeatherData -> dailyWeatherData.data }
    }
}

@SuppressLint("SimpleDateFormat")
fun WeatherResponse.toWeatherComponents(): WeatherComponents {
    val dailyForecast = toDailyForecast()
    val hourlyForecast = toHourlyForecast()
    var todayMaxTemp: Int = 0
    var todayMinTemp: Int = 0
    var currentHourWeatherType: WeatherType = WeatherType.fromWHO(0)
    var currentHourTemp: Int = 0
    dailyForecast[0]?.let {
        todayMaxTemp = it[0].maxTemperature
        todayMinTemp = it[0].minTemperature
    }
    val calendar = Calendar.getInstance()

    val localTime = LocalTime.now()
    val time: Int = if (calendar.get(Calendar.MINUTE) < 30) localTime.hour else localTime.hour + 1
    val currentHourWeather = hourlyForecast[0]!!.firstOrNull() { hourlyForecast ->
        hourlyForecast.currentDate.hour == time
    }
    currentHourTemp = currentHourWeather!!.currentTemp
    currentHourWeatherType = currentHourWeather.weatherType

    return WeatherComponents(
        dailyForecast = dailyForecast, hourlyForecast = hourlyForecast,
        mainWeatherInfo = MainWeatherInfo(
            todayMaxTemp,
            todayMinTemp,
            currentHourWeatherType,
            currentHourTemp,

        )
    )
}

fun LocalTime.toHour(): String {
    val time = this.toString()
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val parsedDate = inputFormat.parse(time)!!
    return outputFormat.format(parsedDate)

}


