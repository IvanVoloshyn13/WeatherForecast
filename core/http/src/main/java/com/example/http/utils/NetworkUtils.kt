package com.example.http.utils

import android.util.Log
import com.example.http.exeptions.ApiException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import retrofit2.Response
import java.io.IOException


public suspend fun <T> executeApiCall(
    call: suspend () -> Response<T>,
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry,
    errorHandler: (Int, String?) -> Exception = ::defaultErrorHandler
): ApiResult<T> {
    repeat(maxAttempts) { attempt ->
        try {
            return call().toResult(errorHandler)
        } catch (e: Exception) {
            if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                if (e is JsonDataException) {
                    Log.e("APICall", e.toString())

                }
                throw e
            }
        }
        val nextDelay = attempt * attempt * defaultDelay
        delay(nextDelay)

    }
    throw IllegalStateException("Unknown exception from executeWithRetry.")
}

private fun defaultShouldRetry(exception: Exception) = when (exception) {
    is ApiException -> exception.code == 429
    is IOException -> true
    else -> false
}

private fun defaultErrorHandler(code: Int, message: String?) = ApiException(code, message)


private fun <T> Response<T>.toResult(errorHandler: (Int, String?) -> Exception): ApiResult<T> {
    return try {
        if (isSuccessful) {
            ApiResult.Success(body()!!)
        } else {
            val error = errorBody()?.let {
                val errorAdapter = Moshi.Builder().build().adapter(Error::class.java)
                errorAdapter.fromJson(it.source())
            }
            ApiResult.Error<ApiException>(message = error?.message)
            throw errorHandler(code(), error?.message)
        }
    } catch (e: Exception) {
        ApiResult.Error<ApiException>(message = e.message, e = e)
        throw errorHandler(code(), null)

    }

}


