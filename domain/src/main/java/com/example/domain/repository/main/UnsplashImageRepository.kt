package com.example.domain.repository.main

import com.example.domain.models.mainscreen.unsplash.CurrentLocationImage
import com.example.domain.utils.Resource

interface UnsplashImageRepository {
    suspend fun getUnsplashCityImageByName(cityName: String): Resource<CurrentLocationImage>
}