package com.example.data.repository.weatherscreen
import com.example.data.di.IoDispatcher
import com.example.data.mappers.toResourceError
import com.example.domain.models.CurrentLocationImage
import com.example.domain.repository.weather.UnsplashImageRepository
import com.example.domain.utils.Resource
import com.example.http.exeptions.ApiException
import com.example.http.utils.ApiResult
import com.example.http.utils.executeApiCall
import com.example.network.apiServices.weather.ApiUnsplashService
import com.example.network.models.unsplash.UnsplashApiResponse
import com.example.network.utils.UnsplashApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class UnsplashImageRepositoryImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UnsplashImageRepository {
    override suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<CurrentLocationImage> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                return@withContext when (result) {
                    is ApiResult.Success -> {
                        if (result.data.imageList.isNotEmpty()) {
                            Resource.Success(data = result.data.toCityImage())
                        } else {
                            Resource.Error(message = "Cant load image")
                        }
                    }

                    is ApiResult.Error -> {
                        Resource.Error(message = result.message, e = result.e)
                    }
                }
            } catch (e: ApiException) {
                return@withContext e.toResourceError()
            }
        }

}

fun UnsplashApiResponse.toCityImage(): CurrentLocationImage {
    val randomImageNumber = this.imageList.size.toRandomNumber()
    val imageUrl = this.imageList[randomImageNumber].imageUrls.small
    return CurrentLocationImage(cityImageUrl = imageUrl)
}

fun Int.toRandomNumber(): Int {
    return if (this > 0) Random.nextInt(0, this) else 0
}