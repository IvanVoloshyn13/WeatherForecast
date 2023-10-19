package com.example.data.repository

import com.example.domain.repository.LocationTimeRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class LocationTimeRepositoryImpl : LocationTimeRepository {
    override suspend fun getLocationTime(timeZoneId: String): Flow<String> {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId))


    }

    override suspend fun getLocationTime(locale: Locale): Flow<String> {

    }
}