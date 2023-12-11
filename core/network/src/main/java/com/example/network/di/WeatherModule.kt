package com.example.network.di

import com.example.network.apiServices.APIWeatherService
import com.example.network.utils.OpenMeteo
import com.example.network.utils.OpenMeteoApi
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
internal object WeatherModule {


    @Provides
    @Singleton
    @OpenMeteoApi
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val callFactory = Call.Factory { request -> okHttpClient.newCall(request) }
        return Retrofit.Builder()
            .baseUrl(OpenMeteo.BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .callFactory(callFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(@OpenMeteoApi retrofit: Retrofit): APIWeatherService =
        retrofit.create(APIWeatherService::class.java)


}
