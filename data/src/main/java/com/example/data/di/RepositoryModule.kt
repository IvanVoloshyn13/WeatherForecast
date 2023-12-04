package com.example.data.di

import com.example.data.repository.LocationTimeRepositoryImpl
import com.example.data.repository.UnsplashImageRepoImpl
import com.example.data.repository.UserLocationTimezoneRepositoryImpl
import com.example.data.repository.WeatherDataRepositoryImpl
import com.example.domain.repository.LocationTimeRepository
import com.example.domain.repository.UnsplashImageRepository
import com.example.domain.repository.UserLocationTimezone
import com.example.domain.repository.WeatherDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindWeatherRepository(weatherDataRepositoryImpl: WeatherDataRepositoryImpl): WeatherDataRepository

    @Binds
    fun bindLocationTimeRepository(locationTimeRepositoryImpl: LocationTimeRepositoryImpl): LocationTimeRepository

    @Binds
    fun userLocationTimezone(userLocationTimezone: UserLocationTimezoneRepositoryImpl): UserLocationTimezone

    @Binds
    fun provideUnsplashImageRepository(unsplashImageRepo: UnsplashImageRepoImpl): UnsplashImageRepository

}