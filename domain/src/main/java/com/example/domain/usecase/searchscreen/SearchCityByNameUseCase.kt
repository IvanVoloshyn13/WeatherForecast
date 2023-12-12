package com.example.domain.usecase.searchscreen

import com.example.domain.repository.search.SearchCity
import javax.inject.Inject

class SearchCityByNameUseCase @Inject constructor(private val searchCity: SearchCity) {
    suspend fun invoke(cityName: String) = searchCity.searchCityByName(cityName)
}