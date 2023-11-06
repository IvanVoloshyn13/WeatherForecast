package com.example.domain.location

import com.example.domain.models.location.CurrentUserLocation
import com.example.domain.utils.Resource


interface FusedLocationProvider {

    suspend fun getCurrentUserLocation(): Resource<CurrentUserLocation>

}