package com.example.network

import com.example.http.models.NetworkErrorResponse
import com.slack.eithernet.ApiResult

typealias NetworkResult<T> = ApiResult<T, NetworkErrorResponse>
