package com.example.domain.repository.weather

import com.example.domain.repository.search.CitiesList

interface GetSavedLocationRepository {
    suspend fun getSavedCityList(): CitiesList
}