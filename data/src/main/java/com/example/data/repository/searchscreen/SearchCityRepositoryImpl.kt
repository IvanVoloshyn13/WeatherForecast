package com.example.data.repository.searchscreen

import com.example.data.di.IoDispatcher
import com.example.data.mappers.toResourceError
import com.example.domain.models.SearchedCity
import com.example.domain.repository.search.CitiesList
import com.example.domain.repository.search.SearchCityRepository
import com.example.domain.utils.Resource
import com.example.http.exeptions.ApiException
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.search.ApiSearchCityService
import com.example.network.models.searchcities.CitiesSearchResponse
import com.example.network.models.searchcities.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SearchCityRepositoryImpl @Inject constructor(
    private val searchCityService: ApiSearchCityService,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : SearchCityRepository {

    override suspend fun searchCityByName(cityName: String): Resource<CitiesList> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(call = {
                    searchCityService.searchCityByName(cityName = cityName)
                })
                return@withContext when (result) {
                    is ApiResult.Success -> {
                        Resource.Success(data = result.data.toSearchedCityList())
                    }

                    is ApiResult.Error -> {
                        Resource.Error(message = result.message)
                    }
                }
            }catch (e: ApiException) {
                return@withContext e.toResourceError()
            }
        }
}

fun CitiesSearchResponse.toSearchedCityList(): ArrayList<SearchedCity> {
    return this.citiesList.map { city: City ->
        SearchedCity(
            id=city.id,
            cityName = city.name,
            latitude = city.latitude,
            longitude = city.longitude,
            timezone = city.timezone,
            country = city.country
        )
    } as ArrayList<SearchedCity>

}