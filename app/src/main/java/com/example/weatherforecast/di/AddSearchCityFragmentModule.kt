package com.example.weatherforecast.di

import com.example.domain.repository.search.SaveCityRepository
import com.example.domain.repository.search.SearchCityRepository
import com.example.domain.usecase.addsearch.SaveCityUseCase
import com.example.domain.usecase.addsearch.SearchCityByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AddSearchCityFragmentModule {
    @Provides
    fun provideSaveCityModelToLocalDatabaseUseCase(saveCityRepository: SaveCityRepository): SaveCityUseCase {
        return SaveCityUseCase(saveCityRepository)
    }

    @Provides
    fun provideSearchCityByNameUseCase(searchCityRepository: SearchCityRepository): SearchCityByNameUseCase {
        return SearchCityByNameUseCase(searchCityRepository)
    }
}