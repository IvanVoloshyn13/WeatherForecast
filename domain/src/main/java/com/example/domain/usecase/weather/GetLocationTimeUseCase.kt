package com.example.domain.usecase.weather

import com.example.domain.repository.weather.LocationTimeRepository
import kotlinx.coroutines.flow.Flow

class GetLocationTimeUseCase(private val locationTimeRepository: LocationTimeRepository) {

     fun invoke(timeZoneId: String, updateTime: Boolean): Flow<String> {
        return locationTimeRepository.getCurrentTimeForLocation(timeZoneId, updateTime)
    }

}