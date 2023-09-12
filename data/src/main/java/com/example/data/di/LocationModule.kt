package com.example.data.di

import com.example.data.location.FusedLocationProviderImpl
import com.example.domain.location.FusedLocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun provideFusedLocation(
        fusedLocationProviderImpl: FusedLocationProviderImpl
    ): FusedLocationProvider
}