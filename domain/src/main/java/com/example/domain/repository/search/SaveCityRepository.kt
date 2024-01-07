package com.example.domain.repository.search

import com.example.domain.models.SearchedCity

interface SaveCityRepository {
    suspend fun saveCity(city: SearchedCity): Long
}