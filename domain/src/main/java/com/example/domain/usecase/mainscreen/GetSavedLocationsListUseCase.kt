package com.example.domain.usecase.mainscreen

import com.example.domain.repository.main.GetSavedLocationRepository

class GetSavedLocationsListUseCase(private val savedLocationRepository: GetSavedLocationRepository) {
    suspend fun invoke() = savedLocationRepository.getSavedCityList()
}