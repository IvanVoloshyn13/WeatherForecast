package com.example.network

import com.example.http.models.NetworkErrorResponse

typealias NetworkResult <T> = ApiResult<T, NetworkErrorResponse>

class ApiResult<T, E>(val data: T, exception: E)