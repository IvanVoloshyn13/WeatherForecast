package com.example.domain.usecase.weather

import com.example.domain.models.SearchedCity
import com.example.domain.repository.weather.GetLocationByIdRepository
import com.example.domain.utils.Resource

class GetLocationByIdUseCase(private val getLocationByIdRepository: GetLocationByIdRepository) {

    suspend fun invoke(id: Int): Resource<SearchedCity> {
        return getLocationByIdRepository.getLocationById(id)
    }
}