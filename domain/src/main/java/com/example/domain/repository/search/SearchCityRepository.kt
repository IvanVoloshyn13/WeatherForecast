package com.example.domain.repository.search

import com.example.domain.models.SearchedCity
import com.example.domain.utils.Resource

typealias CitiesList = List<SearchedCity>

interface SearchCityRepository {
    suspend fun searchCityByName(cityName: String): Resource<CitiesList>
}