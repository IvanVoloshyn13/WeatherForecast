package com.example.domain.repository.search

import com.example.domain.models.searchscreen.SearchedCity

interface SaveCityRepo {
    suspend fun saveCity(city: SearchedCity): Long
}