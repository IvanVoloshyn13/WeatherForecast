package com.example.data.mappers

import android.annotation.SuppressLint
import com.example.domain.models.weather.DailyForecast
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.models.weather.MainWeatherInfo
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.models.weather.WeatherType
import com.example.network.models.WeatherResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
        val dayTime = daily.time.toListOfTheDayWeek()[index]
        val weatherType = WeatherType.fromWHO(daily.weathercode[index])
        IndexedDailyWeatherData(
            index = index,
            data = DailyForecast(
                weatherType = weatherType,
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
    var currentHourWeatherType: WeatherType
    var currentHourTemp: Int = 0
    dailyForecast[0]?.let {
        todayMaxTemp = it[0].maxTemperature
        todayMinTemp = it[0].minTemperature
    }
    val calendar = Calendar.getInstance()
    val localTime = LocalTime.now()
    val time: Int = if (calendar.get(Calendar.MINUTE) < 30) localTime.hour else localTime.hour + 1
    val currentHourWeather = hourlyForecast[0]!!.firstOrNull() { hourly ->
        hourly.currentDate.hour == time
    }
    currentHourTemp = currentHourWeather!!.currentTemp
    currentHourWeatherType = currentHourWeather.weatherType

    return WeatherComponents(
        dailyForecast = dailyForecast[0] as ArrayList<DailyForecast>,
        hourlyForecast = toHourlyForecastList(),
        mainWeatherInfo = MainWeatherInfo(
            todayMaxTemp,
            todayMinTemp,
            currentHourWeatherType,
            currentHourTemp
        )
    )
}

fun WeatherResponse.toHourlyForecastList(): ArrayList<HourlyForecast> {
    val hourlyForecast = this.toHourlyForecast()
    val todayHourly = hourlyForecast[0]
    val nextDayHourly: List<HourlyForecast>?
    val calendar = Calendar.getInstance()
    val localTime = LocalTime.now()
    val time: Int = if (calendar.get(Calendar.MINUTE) < 30)
        localTime.hour else localTime.hour + 1
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




