package com.example.domain.usecase.mainscreen

import com.example.domain.connectivity.NetworkObserver
import javax.inject.Inject

class GetNetworkStatusUseCase @Inject constructor(private val networkObserver: NetworkObserver) {
    fun invoke() = networkObserver.observe()
}