package com.example.data.repository.weatherscreen

import com.example.data.di.IoDispatcher
import com.example.data.repository.searchscreen.toSearchedCity
import com.example.data.storage.room.AppDatabase
import com.example.domain.models.SearchedCity
import com.example.domain.repository.weather.GetLocationByIdRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLocationByIdRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher:CoroutineDispatcher
) : GetLocationByIdRepository {
    override suspend fun getLocationById(id: Int): Resource<SearchedCity> = withContext(ioDispatcher) {
        val city = database.getCityDao().getCityById(id)
        return@withContext if (city != null) {
            Resource.Success(data = city.toSearchedCity())
        } else {
            Resource.Error(message = "Element not found")
        }
    }
}