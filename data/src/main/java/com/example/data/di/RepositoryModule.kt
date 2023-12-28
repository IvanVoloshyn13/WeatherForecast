package com.example.data.di

import com.example.data.location.FusedLocationProviderImpl
import com.example.data.repository.mainscreen.LocationTimeRepositoryImpl
import com.example.data.repository.mainscreen.UnsplashImageRepoImpl
import com.example.data.repository.mainscreen.WeatherDataRepositoryImpl
import com.example.data.repository.searchscreen.LocalDatabaseOperationImpl
import com.example.data.repository.searchscreen.SearchCityRepoImpl
import com.example.domain.location.FusedLocationProvider
import com.example.domain.repository.main.LocationTimeRepository
import com.example.domain.repository.main.UnsplashImageRepository
import com.example.domain.repository.main.WeatherDataRepository
import com.example.domain.repository.search.SaveCityRepo
import com.example.domain.repository.search.SearchCityRepo
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
    fun provideUnsplashImageRepository(unsplashImageRepo: UnsplashImageRepoImpl): UnsplashImageRepository

    @Binds
    fun provideSearchCityRepository(searchCity: SearchCityRepoImpl): SearchCityRepo

    @Binds
    fun provideSaveCityRepo(localDatabaseOperationImpl: LocalDatabaseOperationImpl): SaveCityRepo

}