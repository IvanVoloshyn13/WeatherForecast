package com.example.data.repository.mainscreen

import com.example.domain.repository.main.LocationTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class LocationTimeRepositoryImpl @Inject constructor() : LocationTimeRepository {
    override fun getLocationTime(timeZoneId: String, updateTime: Boolean) = flow<String> {
        val timeZone = TimeZone.getTimeZone(timeZoneId).toZoneId()
        var time = LocalTime.now(timeZone)
        while (updateTime) {
            delay(1000)
            time = time.plusSeconds(1)
            val formattedTime = time.toHour()
            this.emit(formattedTime)
        }
    }.flowOn(Dispatchers.IO)



    private fun LocalTime.toHour(): String {
        val time = this.toString()
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedDate = inputFormat.parse(time)!!
        return outputFormat.format(parsedDate)

    }


}