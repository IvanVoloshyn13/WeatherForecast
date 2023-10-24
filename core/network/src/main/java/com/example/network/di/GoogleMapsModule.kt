package com.example.network.di

import com.example.network.apiServices.ApiGoogleTimezoneService
import com.example.network.utils.Google
import com.example.network.utils.GoogleMaps
import com.slack.eithernet.ApiResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GoogleMapsModule {

    @Singleton
    @Provides
    @GoogleMaps
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val callFactory = Call.Factory { request -> okHttpClient.newCall(request) }
        return Retrofit.Builder().baseUrl(Google.GOOGLE_TIMEZONE_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .callFactory(callFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleService(@GoogleMaps retrofit: Retrofit) =
        retrofit.create(ApiGoogleTimezoneService::class.java)
}