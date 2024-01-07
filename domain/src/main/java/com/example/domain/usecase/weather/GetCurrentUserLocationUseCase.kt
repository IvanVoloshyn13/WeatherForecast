package com.example.domain.usecase.weather

import com.example.domain.location.FusedLocationProvider
import com.example.domain.models.CurrentUserLocation
import com.example.domain.utils.Resource

class GetCurrentUserLocationUseCase(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke(): Resource<CurrentUserLocation> {
        return fusedLocationProvider.getCurrentUserLocation()
    }
}