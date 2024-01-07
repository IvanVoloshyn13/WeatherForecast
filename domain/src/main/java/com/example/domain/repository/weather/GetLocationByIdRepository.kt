package com.example.domain.repository.weather

import com.example.domain.models.SearchedCity
import com.example.domain.utils.Resource

interface GetLocationByIdRepository {
    suspend fun getLocationById(id: Int): Resource<SearchedCity>
}