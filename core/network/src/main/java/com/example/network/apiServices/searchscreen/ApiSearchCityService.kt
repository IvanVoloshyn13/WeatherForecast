package com.example.network.apiServices.searchscreen

import com.example.network.models.searchcities.CitiesSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSearchCityService {
    @GET("v1/search?")
    suspend fun searchCityByName(
        @Query("name") cityName: String,
        @Query("count") listSize: Int = CITY_SEARCH_LIST_SIZE,
        @Query("language") language: String = LANGUAGE,
        @Query("format") format: String = FORMAT,
    ): Response<CitiesSearchResponse>

    companion object {
        const val CITY_SEARCH_LIST_SIZE = 20
        const val LANGUAGE = "en"
        const val FORMAT = "json"
    }
}