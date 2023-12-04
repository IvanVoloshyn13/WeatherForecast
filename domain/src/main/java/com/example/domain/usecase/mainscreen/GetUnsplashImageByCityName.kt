package com.example.domain.usecase.mainscreen

import com.example.domain.repository.UnsplashImageRepository
import javax.inject.Inject

class GetUnsplashImageByCityName @Inject constructor(private val unsplashImageRepository: UnsplashImageRepository) {
    suspend fun invoke(cityName: String) =
        unsplashImageRepository.getUnsplashCityImageByName(cityName)
}