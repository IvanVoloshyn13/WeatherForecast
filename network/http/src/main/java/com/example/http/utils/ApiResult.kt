package com.example.http.utils

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String?, val e: Exception? = null) : ApiResult<T>()
}




