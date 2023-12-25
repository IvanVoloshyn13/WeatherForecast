package com.example.data.mappers

import com.example.domain.utils.Resource
import com.example.http.exeptions.ApiException

fun <T> ApiException.toResourceError(): Resource.Error<T> {

    return if (this.isClientError()) {
        Resource.Error(message = "The requested resource was not found", e = this)
    } else if (this.isServerError()) {
        Resource.Error(message = "An unexpected error occurred on the server", e = this)
    } else {
        Resource.Error(message = "Unknown exception")
    }
}