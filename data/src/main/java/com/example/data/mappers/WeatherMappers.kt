package com.example.data.mappers

import android.annotation.SuppressLint
import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo
import com.example.domain.models.weather.WeatherComponents
import com.example.network.models.weather.WeatherResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone
import java.util.stream.Stream

private data class IndexedHourlyWeatherData(
    val index: Int,
    val data: HourlyForecast
)

private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyForecast
)


fun WeatherResponse.toHourlyForecast(): Map<Int, List<HourlyForecast>> {
    return hourly.time.mapIndexed { index, time ->
        val currentTemp = hourly.temperature_2m[index]
        val weatherCode = hourly.weathercode[index]
        IndexedHourlyWeatherData(
            index = index,
            data = HourlyForecast(
                currentDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                currentTemp = currentTemp.toInt(),
                weatherCode = weatherCode
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
        val dayTime = daily.time.toListOfTheDayWeek()[index]
        val weatherCode = daily.weathercode[index]
        IndexedDailyWeatherData(
            index = index,
            data = DailyForecast(
                weatherCode = weatherCode,
                maxTemperature = maxTemperature,
                minTemperature = minTemperature,
                dayOfTheWeek = dayTime
            )
        )
    }.groupBy {
        it.index / 7
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
    var currentHourWeatherCode: Int = 0
    var currentHourTemp: Int = 0
    dailyForecast[0]?.let {
        todayMaxTemp = it[0].maxTemperature
        todayMinTemp = it[0].minTemperature
    }
    val timeZoneId = timezone
    val time = getCurrentLocalTime(timeZoneId)
    val currentHourWeather = hourlyForecast[0]?.firstOrNull() { hourly ->
        hourly.currentDate.hour == time
    }
    if (currentHourWeather != null) {
        currentHourTemp = currentHourWeather.currentTemp
        currentHourWeatherCode = currentHourWeather.weatherCode
    }

    return WeatherComponents(
        dailyForecast = dailyForecast[0] as ArrayList<DailyForecast>,
        hourlyForecast = toHourlyForecastList(),
        mainWeatherInfo = MainWeatherInfo(
            todayMaxTemp,
            todayMinTemp,
            currentHourWeatherCode,
            currentHourTemp
        ),
        timezone = timezone
    )
}

fun WeatherResponse.toHourlyForecastList(): ArrayList<HourlyForecast> {
    val hourlyForecast = this.toHourlyForecast()
    val todayHourly = hourlyForecast[0]
    val nextDayHourly: List<HourlyForecast>?
    val time = getCurrentLocalTime(timezone)
    return if (time > 0) {
        val resultHourly: ArrayList<HourlyForecast> = ArrayList()
        nextDayHourly = hourlyForecast[1]?.dropLast(24 - time)
        val todayHourlyWithDropElements = todayHourly?.drop(time)
        Stream.of(todayHourlyWithDropElements, nextDayHourly)
            .forEach { item -> resultHourly.addAll(item as Collection<HourlyForecast>) }
        resultHourly
    } else {
        todayHourly as ArrayList<HourlyForecast>
    }
}

typealias ListOfTheWeekDate = List<String>

fun ListOfTheWeekDate.toListOfTheDayWeek(): ArrayList<String> {
    val daysList = ArrayList<String>()
    forEach { item ->
        val weekDay = LocalDate.parse(item).dayOfWeek.name
        daysList.add(weekDay)
    }
    return daysList
}

private fun getCurrentLocalTime(timeZoneId: String): Int {
    val timeZone = TimeZone.getTimeZone(timeZoneId)
    val zoneId = TimeZone.getTimeZone(timeZoneId).toZoneId()
    val calendar = Calendar.getInstance(timeZone)
    val localTime = LocalTime.now(zoneId)
    return if (calendar.get(Calendar.MINUTE) < 30)
        localTime.hour else localTime.hour + 1
}



