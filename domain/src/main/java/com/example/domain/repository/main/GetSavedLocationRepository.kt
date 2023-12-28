package com.example.domain.repository.main

import com.example.domain.models.searchscreen.SearchedCity

interface GetSavedLocationRepository {
    suspend fun getSavedCityList(): ArrayList<SearchedCity>
}