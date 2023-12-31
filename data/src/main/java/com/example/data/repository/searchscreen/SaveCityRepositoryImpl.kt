package com.example.data.repository.searchscreen

import com.example.data.di.IoDispatcher
import com.example.data.storage.room.AppDatabase
import com.example.data.storage.room.CityEntity
import com.example.domain.models.SearchedCity
import com.example.domain.repository.search.SaveCityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveCityRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SaveCityRepository {
    override suspend fun saveCity(city: SearchedCity): Long = withContext(ioDispatcher) {
        return@withContext database.getCityDao().saveNewCity(city.toCityEntity())
    }
}

fun SearchedCity.toCityEntity(): CityEntity {
    return CityEntity(
        id = id,
        cityName = cityName,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country
    )
}

fun CityEntity.toSearchedCity(): SearchedCity {
    return SearchedCity(
        id = id,
        cityName = cityName,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country
    )
}
