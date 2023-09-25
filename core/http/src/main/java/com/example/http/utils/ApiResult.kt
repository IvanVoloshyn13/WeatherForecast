package com.example.http.utils

import java.lang.Exception

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val exception: Exception) : ApiResult<Nothing>()
}
