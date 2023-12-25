package com.example.domain.utils

import java.lang.Exception

sealed class Resource<T>(
) {

    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val e: Exception? = null, val message: String?) : Resource<T>()
}
