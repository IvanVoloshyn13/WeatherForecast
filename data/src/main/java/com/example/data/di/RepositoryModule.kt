package com.example.data.di

import com.example.data.repository.mainscreen.LocationTimeRepositoryImpl
import com.example.data.repository.mainscreen.UnsplashImageRepoImpl
import com.example.data.repository.mainscreen.UserLocationTimezoneRepositoryImpl
import com.example.data.repository.mainscreen.WeatherDataRepositoryImpl
import com.example.data.repository.searchscreen.LocalDatabaseOperationImpl
import com.example.data.repository.searchscreen.SearchCityImpl
import com.example.domain.repository.main.LocationTimeRepository
import com.example.domain.repository.main.UnsplashImageRepository
import com.example.domain.repository.main.UserLocationTimezone
import com.example.domain.repository.main.WeatherDataRepository
import com.example.domain.repository.search.SaveCity
import com.example.domain.repository.search.SearchCity
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

    @Binds
    fun provideSearchCityRepository(searchCity: SearchCityImpl): SearchCity

    @Binds
    fun provideSaveCityRepo(localDatabaseOperationImpl: LocalDatabaseOperationImpl): SaveCity

}