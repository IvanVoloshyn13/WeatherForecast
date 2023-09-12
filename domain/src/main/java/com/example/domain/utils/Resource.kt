package com.example.domain.utils

sealed class Resource<T>(
    val data: T?,
    val message: String? = null
) {
    class Loading<T>() : Resource<T>(null, null)
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)
}
