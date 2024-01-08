package com.example.data.repository.weatherscreen

import com.example.data.repository.searchscreen.toSearchedCity
import com.example.data.storage.room.AppDatabase
import com.example.domain.models.SearchedCity
import com.example.domain.repository.weather.GetSavedLocationRepository
import com.example.domain.utils.Resource
import java.io.IOException
import javax.inject.Inject

class GetSavedLocationRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : GetSavedLocationRepository {
    override suspend fun getSavedCityList(): Resource<List<SearchedCity>> {
        return try {
            val cityListEntity = database.getCityDao().getAllCities()
            val searchedCities = cityListEntity.map {
                it.toSearchedCity()
            }
            Resource.Success(data = searchedCities)
        } catch (e: IOException) {
            Resource.Error(e = e, message = e.message)
        }

    }
}