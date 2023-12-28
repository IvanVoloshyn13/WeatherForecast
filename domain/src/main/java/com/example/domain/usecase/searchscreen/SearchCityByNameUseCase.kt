package com.example.domain.usecase.searchscreen

import com.example.domain.repository.search.SearchCityRepo

class SearchCityByNameUseCase(private val searchCityRepo: SearchCityRepo) {
    suspend fun invoke(cityName: String) = searchCityRepo.searchCityByName(cityName)
}