package com.example.domain.usecase.mainscreen

import com.example.domain.location.FusedLocationProvider
import com.example.domain.models.mainscreen.location.CurrentUserLocation
import com.example.domain.utils.Resource
import javax.inject.Inject

class GetCurrentUserLocationUseCase @Inject constructor(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke():Resource<CurrentUserLocation> {
        return fusedLocationProvider.getCurrentUserLocation()
    }
}