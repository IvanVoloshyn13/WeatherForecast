package com.example.data.di

import com.example.data.repository.LocationTimeRepositoryImpl
import com.example.data.repository.WeatherDataRepositoryImpl
import com.example.domain.repository.LocationTimeRepository
import com.example.domain.repository.WeatherDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindWeatherRepository(weatherDataRepositoryImpl: WeatherDataRepositoryImpl): WeatherDataRepository

    @Binds
    abstract fun bindLocationTimeRepository(locationTimeRepositoryImpl: LocationTimeRepositoryImpl): LocationTimeRepository
}