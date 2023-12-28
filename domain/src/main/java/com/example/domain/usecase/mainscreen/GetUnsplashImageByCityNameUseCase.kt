package com.example.domain.usecase.mainscreen

import com.example.domain.repository.main.UnsplashImageRepository

class GetUnsplashImageByCityNameUseCase(
    private val unsplashImageRepository: UnsplashImageRepository
) {
    suspend fun invoke(cityName: String) =
        unsplashImageRepository.getUnsplashCityImageByName(cityName)
}