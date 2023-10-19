package com.example.domain.usecase.mainscreen

import com.example.domain.repository.LocationTimeRepository
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

class GetLocationTimeUseCase @Inject constructor(val locationTimeRepository: LocationTimeRepository) {

    suspend fun invoke(timeZoneId: String): Flow<String> {
        return locationTimeRepository.getLocationTime(timeZoneId)
    }

    suspend fun invoke(locale: Locale): Flow<String> {
        return locationTimeRepository.getLocationTime(locale)
    }
}