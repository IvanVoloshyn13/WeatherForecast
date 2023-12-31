package com.example.data.repository.mainscreen

import com.example.data.repository.searchscreen.toSearchedCity
import com.example.data.storage.room.AppDatabase
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.repository.main.GetSavedLocationRepository
import javax.inject.Inject

class GetSavedLocationImpl @Inject constructor(
    private val database: AppDatabase
) : GetSavedLocationRepository {
    override suspend fun getSavedCityList(): ArrayList<SearchedCity> {
        val cityListEntity = database.getCityDao().getAllCities()
        return cityListEntity.map {
            it.toSearchedCity()
        } as ArrayList<SearchedCity>
    }
}