package com.example.weatherforecast.di

import com.example.domain.location.FusedLocationProvider
import com.example.domain.repository.main.GetSavedLocationRepository
import com.example.domain.repository.main.LocationTimeRepository
import com.example.domain.repository.main.UnsplashImageRepository
import com.example.domain.repository.main.WeatherDataRepository
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetLocationTimeUseCase
import com.example.domain.usecase.mainscreen.GetSavedLocationsListUseCase
import com.example.domain.usecase.mainscreen.GetUnsplashImageByCityNameUseCase
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainFragmentUseCaseModule {
    @Provides
    fun provideGetWeatherDataUseCase(weatherDataRepository: WeatherDataRepository): GetWeatherDataUseCase {
        return GetWeatherDataUseCase(weatherDataRepository)
    }

    @Provides
    fun provideUnsplashImageByCityNameUseCase(unsplashImageRepository: UnsplashImageRepository): GetUnsplashImageByCityNameUseCase {
        return GetUnsplashImageByCityNameUseCase(
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
}