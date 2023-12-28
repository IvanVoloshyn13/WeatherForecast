package com.example.domain.usecase.searchscreen

import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.repository.search.SaveCityRepo

class SaveCityModelToLocalDatabaseUseCase(private val saveCityRepo: SaveCityRepo) {
    suspend fun invoke(city: SearchedCity): Long {
        return saveCityRepo.saveCity(city)
    }
}