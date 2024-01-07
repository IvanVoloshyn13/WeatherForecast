package com.example.data.repository.weatherscreen

import com.example.data.repository.searchscreen.toSearchedCity
import com.example.data.storage.room.AppDatabase
import com.example.domain.models.SearchedCity
import com.example.domain.repository.weather.GetSavedLocationRepository
import javax.inject.Inject

class GetSavedLocationRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : GetSavedLocationRepository {
    override suspend fun getSavedCityList(): List<SearchedCity> {
        val cityListEntity = database.getCityDao().getAllCities()
        return cityListEntity.map {
            it.toSearchedCity()
        }
    }
}