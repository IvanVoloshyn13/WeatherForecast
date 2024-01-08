package com.example.network.apiServices.weather

import com.example.network.models.unsplash.UnsplashApiResponse
import com.example.network.utils.Unsplash
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiUnsplashService {
    @GET("/search/photos")
    @Headers("Accept-Version: v1")
    suspend fun fetchPictureByLocation(
        @Query("client_id") clientId: String=Unsplash.CLIENT_ID,
        @Query("query") cityName: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("orientation") orientation: String = "portrait",
        ): Response<UnsplashApiResponse>
}