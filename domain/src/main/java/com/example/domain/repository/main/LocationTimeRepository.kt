package com.example.domain.repository.main

import kotlinx.coroutines.flow.Flow
import java.util.Locale


interface LocationTimeRepository {
     fun getLocationTime(timeZoneId:String, updateTime:Boolean): Flow<String>

}