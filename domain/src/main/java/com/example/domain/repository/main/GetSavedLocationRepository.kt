package com.example.domain.repository.main

import com.example.domain.repository.search.CitiesList

interface GetSavedLocationRepository {
    suspend fun getSavedCityList(): CitiesList
}