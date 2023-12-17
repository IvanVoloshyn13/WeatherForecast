package com.example.data.repository.mainscreen

import com.example.data.di.IoDispatcher
import com.example.domain.models.mainscreen.unsplash.CityImage
import com.example.domain.repository.main.UnsplashImageRepository
import com.example.domain.utils.Resource
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.mainscreen.ApiUnsplashService
import com.example.network.models.unsplash.UnsplashApiResponse
import com.example.network.utils.UnsplashApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class UnsplashImageRepoImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UnsplashImageRepository {
    override suspend fun getUnsplashCityImageByName(cityName: String): Resource<CityImage?> =
        withContext(dispatcher) {
            val result = executeApiCall(
                { apiUnsplashService.getPictureByLocation(cityName = cityName) }
            )
            return@withContext when (result) {
                is ApiResult.Success -> {
                    Resource.Success(data = result.data.toCityImage())
                }

                is ApiResult.Error -> {
                    Resource.Error(message = "Something goes wrong. Try again")
                }
            }
        }
}

fun UnsplashApiResponse.toCityImage(): CityImage? {
    return if (this.imageList.isNotEmpty()) {
        val randomImageNumber = this.imageList.size.toRandomNumber()
        val imageUrl = this.imageList[randomImageNumber].imageUrls.small
        CityImage(imageUrl)
    } else null
}

fun Int.toRandomNumber(): Int {
    return if (this > 0) Random.nextInt(0, this) else 0
}