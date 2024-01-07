package com.example.data.di

import com.example.data.location.FusedLocationProviderImpl
import com.example.data.repository.searchscreen.SaveCityRepositoryImpl
import com.example.data.repository.searchscreen.SearchCityRepositoryImpl
import com.example.data.repository.weatherscreen.GetLocationByIdRepositoryImpl
import com.example.data.repository.weatherscreen.GetSavedLocationRepositoryImpl
import com.example.data.repository.weatherscreen.LocationTimeRepositoryImpl
import com.example.data.repository.weatherscreen.UnsplashImageRepositoryImpl
import com.example.data.repository.weatherscreen.WeatherDataRepositoryImpl
import com.example.domain.location.FusedLocationProvider
import com.example.domain.repository.search.SaveCityRepository
import com.example.domain.repository.search.SearchCityRepository
import com.example.domain.repository.weather.GetLocationByIdRepository
import com.example.domain.repository.weather.GetSavedLocationRepository
import com.example.domain.repository.weather.LocationTimeRepository
import com.example.domain.repository.weather.UnsplashImageRepository
import com.example.domain.repository.weather.WeatherDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    abstract fun provideFusedLocation(
        fusedLocationProviderImpl: FusedLocationProviderImpl
    ): FusedLocationProvider

    @Binds
    fun bindWeatherRepository(weatherDataRepositoryImpl: WeatherDataRepositoryImpl): WeatherDataRepository

    @Binds
    fun bindLocationTime(locationTimeRepository: LocationTimeRepositoryImpl): LocationTimeRepository

    @Binds
    fun provideUnsplashImageRepository(unsplashImageRepo: UnsplashImageRepositoryImpl): UnsplashImageRepository

    @Binds
    fun provideSearchCityRepository(searchCity: SearchCityRepositoryImpl): SearchCityRepository

    @Binds
    fun provideSaveCityRepo(saveCityRepositoryImpl: SaveCityRepositoryImpl): SaveCityRepository

    @Binds
    fun bindGetSavedLocationRepository(getSavedLocationRepositoryImpl: GetSavedLocationRepositoryImpl): GetSavedLocationRepository

    @Binds
    fun bindGetLocationByIdRepository(getLocationByIdRepositoryImpl: GetLocationByIdRepositoryImpl): GetLocationByIdRepository

}