package com.example.domain.usecase.addsearch

import com.example.domain.repository.search.SearchCityRepository

class SearchCityByNameUseCase(private val searchCityRepository: SearchCityRepository) {
    suspend fun invoke(cityName: String) = searchCityRepository.searchCityByName(cityName)
}