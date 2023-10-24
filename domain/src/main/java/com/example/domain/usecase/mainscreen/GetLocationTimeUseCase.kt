package com.example.domain.usecase.mainscreen

import com.example.domain.repository.LocationTimeRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetLocationTimeUseCase @Inject constructor(val locationTimeRepository: LocationTimeRepository) {

    suspend fun invoke(timeZoneId: String ,updateTime: Boolean): Flow<String> {
        return locationTimeRepository.getLocationTime(timeZoneId, updateTime)
    }


}