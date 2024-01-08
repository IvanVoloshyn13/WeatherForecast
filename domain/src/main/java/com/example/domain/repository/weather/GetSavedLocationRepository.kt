package com.example.domain.repository.weather

import com.example.domain.repository.search.CitiesList
import com.example.domain.utils.Resource

interface GetSavedLocationRepository {
    suspend fun getSavedCityList(): Resource<CitiesList>
}