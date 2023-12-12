package com.example.network.di

import com.example.network.apiServices.mainscreen.ApiUnsplashService
import com.example.network.utils.Unsplash
import com.example.network.utils.UnsplashApi
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
class UnsplashModule {

    @Provides
    @Singleton
    @UnsplashApi
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val callFactory = Call.Factory { request -> okHttpClient.newCall(request) }
        return Retrofit.Builder()
            .baseUrl(Unsplash.BASE_API)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .callFactory(callFactory)
            .build()
    }

    @Provides
    @Singleton
    @UnsplashApi
    fun provideUnsplashService(@UnsplashApi  retrofit: Retrofit): ApiUnsplashService =
        retrofit.create(ApiUnsplashService::class.java)
}