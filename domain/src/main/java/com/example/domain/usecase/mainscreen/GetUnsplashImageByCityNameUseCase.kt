package com.example.domain.usecase.mainscreen

import com.example.domain.repository.main.UnsplashImageRepository
import javax.inject.Inject

class GetUnsplashImageByCityNameUseCase @Inject constructor(private val unsplashImageRepository: UnsplashImageRepository) {
    suspend fun invoke(cityName: String) =
        unsplashImageRepository.getUnsplashCityImageByName(cityName)
}