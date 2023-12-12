package com.example.data.repository.searchscreen

import com.example.data.di.IoDispatcher
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.repository.search.CitiesList
import com.example.domain.repository.search.SearchCity
import com.example.domain.utils.Resource
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.searchscreen.ApiSearchCityService
import com.example.network.models.searchcities.CitiesSearchResponse
import com.example.network.models.searchcities.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SearchCityImpl @Inject constructor(
    private val searchCityService: ApiSearchCityService,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : SearchCity {

    override suspend fun searchCityByName(cityName: String): Resource<CitiesList> =
        withContext(dispatcher) {
            val result = executeApiCall(call = {
                searchCityService.searchCityByName(cityName = cityName)
            })
            return@withContext when (result) {
                is ApiResult.Success -> {
                    Resource.Success(result.data.toSearchedCityList())
                }

                is ApiResult.Error -> {
                    Resource.Error(result.message)
                }
            }
        }
}

fun CitiesSearchResponse.toSearchedCityList(): ArrayList<SearchedCity> {
    return this.citiesList.map { city: City ->
        SearchedCity(
            cityName = city.name,
            latitude = city.latitude,
            longitude = city.longitude,
            timezone = city.timezone,
            country = city.country
        )
    } as ArrayList<SearchedCity>

}