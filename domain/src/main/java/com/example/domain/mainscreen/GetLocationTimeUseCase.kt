package com.example.domain.mainscreen

import com.example.domain.repository.LocationTimeRepository
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

class GetLocationTimeUseCase @Inject constructor(private val locationTimeRepository: LocationTimeRepository) {

     fun invoke(timeZoneId: String, updateTime:Boolean): Flow<String> {
        return locationTimeRepository.getLocationTime(timeZoneId, updateTime)
    }


}