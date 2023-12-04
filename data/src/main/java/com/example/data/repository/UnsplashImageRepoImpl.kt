package com.example.data.repository

import com.example.domain.models.unsplash.CityImage
import com.example.domain.repository.UnsplashImageRepository
import com.example.domain.utils.Resource
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.ApiUnsplashService
import com.example.network.models.unsplash.UnsplashApiResponse
import com.example.network.utils.UnsplashApi
import javax.inject.Inject
import kotlin.random.Random

class UnsplashImageRepoImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService
) : UnsplashImageRepository {
    override suspend fun getUnsplashCityImageByName(cityName: String): Resource<CityImage> {
        val result = executeApiCall(
            { apiUnsplashService.getPictureByLocation(cityName=cityName) }
        )
        return when (result) {
            is ApiResult.Success -> {
                Resource.Success(data = result.data.toCityImage())

            }

            is ApiResult.Error -> {
                Resource.Error(data = null, message = "Something goes wrong. Try again")
            }
        }
    }
}

fun UnsplashApiResponse.toCityImage(): CityImage {
    val randomImageNumber = this.imageList.size.toRandomNumber()
    val imageUrl = this.imageList[randomImageNumber].imageUrls.regular
    return CityImage(cityImage = imageUrl)

}

fun Int.toRandomNumber(): Int {
    return Random.nextInt(0, this)
}