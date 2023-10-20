package com.example.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.Locale


interface LocationTimeRepository {
     fun getLocationTime(timeZoneId:String, updateTime:Boolean): Flow<String>
     fun getLocationTime(locale: Locale, updateTime:Boolean): Flow<String>
}