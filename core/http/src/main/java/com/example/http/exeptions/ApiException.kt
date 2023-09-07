package com.example.http.exeptions

import java.lang.Exception

class ApiException(val code: Int, message: String?) : Exception(message) {

    public fun isClientError(): Boolean = code in 400..499

    public fun isServerError(): Boolean = code >= 500
}