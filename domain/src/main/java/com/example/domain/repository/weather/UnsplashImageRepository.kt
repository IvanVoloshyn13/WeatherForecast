package com.example.domain.repository.weather

import com.example.domain.models.CurrentLocationImage
import com.example.domain.utils.Resource

interface UnsplashImageRepository {
    suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<CurrentLocationImage>
}