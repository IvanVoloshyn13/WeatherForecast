package com.example.weatherforecast.di

import com.example.domain.location.FusedLocationProvider
import com.example.domain.repository.weather.GetLocationByIdRepository
import com.example.domain.repository.weather.GetSavedLocationRepository
import com.example.domain.repository.weather.LocationTimeRepository
import com.example.domain.repository.weather.UnsplashImageRepository
import com.example.domain.repository.weather.WeatherDataRepository
import com.example.domain.usecase.weather.FetchUnsplashImageByCityNameUseCase
import com.example.domain.usecase.weather.FetchWeatherDataUseCase
import com.example.domain.usecase.weather.GetCurrentUserLocationUseCase
import com.example.domain.usecase.weather.GetLocationByIdUseCase
import com.example.domain.usecase.weather.GetLocationTimeUseCase
import com.example.domain.usecase.weather.GetSavedLocationsListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WeatherFragmentUseCaseModule {
    @Provides
    fun provideGetWeatherDataUseCase(weatherDataRepository: WeatherDataRepository): FetchWeatherDataUseCase {
        return FetchWeatherDataUseCase(weatherDataRepository)
    }

    @Provides
    fun provideUnsplashImageByCityNameUseCase(unsplashImageRepository: UnsplashImageRepository): FetchUnsplashImageByCityNameUseCase {
        return FetchUnsplashImageByCityNameUseCase(
            unsplashImageRepository
        )
    }

    @Provides
    fun provideGetSavedLocationsListUseCase(savedLocationRepository: GetSavedLocationRepository): GetSavedLocationsListUseCase {
        return GetSavedLocationsListUseCase(savedLocationRepository)
    }

    @Provides
    fun provideGetLocationTimeUseCase(locationTimeRepository: LocationTimeRepository): GetLocationTimeUseCase {
        return GetLocationTimeUseCase(locationTimeRepository)
    }

    @Provides
    fun provideGetCurrentUserLocationUseCase(fusedLocationProvider: FusedLocationProvider): GetCurrentUserLocationUseCase {
        return GetCurrentUserLocationUseCase(fusedLocationProvider)
    }

    @Provides
    fun provideGetLocationByIdUseCase(getLocationByIdRepository: GetLocationByIdRepository): GetLocationByIdUseCase {
        return GetLocationByIdUseCase(getLocationByIdRepository)
    }
}