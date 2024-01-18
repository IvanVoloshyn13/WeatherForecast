package com.example.domain.usecase.weather

import com.example.domain.repository.weather.GetSavedLocationRepository

class GetSavedLocationsListUseCase(private val savedLocationRepository: GetSavedLocationRepository) {
    suspend fun invoke() = savedLocationRepository.getSavedCityList()
}