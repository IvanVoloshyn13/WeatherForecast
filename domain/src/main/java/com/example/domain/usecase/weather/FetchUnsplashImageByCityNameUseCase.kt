package com.example.domain.usecase.weather

import com.example.domain.repository.weather.UnsplashImageRepository

class FetchUnsplashImageByCityNameUseCase(
    private val unsplashImageRepository: UnsplashImageRepository
) {
    suspend fun invoke(cityName: String) =
        unsplashImageRepository.fetchUnsplashCityImageByName(cityName)
}