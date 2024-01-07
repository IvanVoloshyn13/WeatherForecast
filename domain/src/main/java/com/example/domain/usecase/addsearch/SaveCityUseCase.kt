package com.example.domain.usecase.addsearch

import com.example.domain.models.SearchedCity
import com.example.domain.repository.search.SaveCityRepository

class SaveCityUseCase(private val saveCityRepository: SaveCityRepository) {
    suspend fun invoke(city: SearchedCity): Long {
        return saveCityRepository.saveCity(city)
    }
}