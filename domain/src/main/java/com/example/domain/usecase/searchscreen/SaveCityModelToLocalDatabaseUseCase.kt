package com.example.domain.usecase.searchscreen

import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.repository.search.SaveCity
import javax.inject.Inject

class SaveCityModelToLocalDatabaseUseCase @Inject constructor(private val saveCity: SaveCity) {
    suspend fun invoke(city: SearchedCity): Long {
        return saveCity.saveCity(city)
    }
}