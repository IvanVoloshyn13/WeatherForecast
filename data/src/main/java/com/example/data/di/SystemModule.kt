package com.example.data.di

import com.example.data.location.FusedLocationProviderImpl
import com.example.data.network.ConnectivityNetworkObserver
import com.example.domain.connectivity.NetworkObserver
import com.example.domain.location.FusedLocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SystemModule {

    @Binds
    abstract fun provideFusedLocation(
        fusedLocationProviderImpl: FusedLocationProviderImpl
    ): FusedLocationProvider

    @Binds
    abstract fun provideConnectivityObserver(connectivityNetworkObserver: ConnectivityNetworkObserver): NetworkObserver


}