package com.example.domain.usecase.mainscreen

import com.example.domain.repository.LocationTimeRepository
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

class GetLocationTimeUseCase @Inject constructor(private val locationTimeRepository: LocationTimeRepository) {

     fun invoke(timeZoneId: String, updateTime:Boolean): Flow<String> {
        return locationTimeRepository.getLocationTime(timeZoneId, updateTime)
    }

     fun invoke(locale: Locale, updateTime:Boolean): Flow<String> {
        return locationTimeRepository.getLocationTime(locale, updateTime)
    }
}