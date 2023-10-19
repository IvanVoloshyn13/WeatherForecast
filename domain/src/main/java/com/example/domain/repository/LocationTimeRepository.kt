package com.example.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.Locale


interface LocationTimeRepository {
    suspend fun getLocationTime(timeZoneId:String): Flow<String>
    suspend fun getLocationTime(locale: Locale): Flow<String>
}