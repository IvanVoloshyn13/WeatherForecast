package com.example.domain.repository.search

import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.utils.Resource

typealias CitiesList = ArrayList<SearchedCity>

interface SearchCityRepo {
    suspend fun searchCityByName(cityName: String): Resource<CitiesList>
}