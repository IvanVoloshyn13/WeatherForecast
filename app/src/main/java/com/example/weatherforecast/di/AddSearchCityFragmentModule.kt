package com.example.weatherforecast.di

import com.example.domain.repository.search.SaveCityRepo
import com.example.domain.repository.search.SearchCityRepo
import com.example.domain.usecase.searchscreen.SaveCityModelToLocalDatabaseUseCase
import com.example.domain.usecase.searchscreen.SearchCityByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AddSearchCityFragmentModule {
    @Provides
    fun provideSaveCityModelToLocalDatabaseUseCase(saveCityRepo: SaveCityRepo): SaveCityModelToLocalDatabaseUseCase {
        return SaveCityModelToLocalDatabaseUseCase(saveCityRepo)
    }

    @Provides
    fun provideSearchCityByNameUseCase(searchCityRepo: SearchCityRepo): SearchCityByNameUseCase {
        return SearchCityByNameUseCase(searchCityRepo)
    }
}