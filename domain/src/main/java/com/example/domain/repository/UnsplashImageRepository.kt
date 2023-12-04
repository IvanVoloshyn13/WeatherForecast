package com.example.domain.repository

import com.example.domain.models.unsplash.CityImage
import com.example.domain.utils.Resource

interface UnsplashImageRepository {
    suspend fun getUnsplashCityImageByName(cityName: String): Resource<CityImage>
}