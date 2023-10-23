package com.example.network.di

import com.example.network.apiServices.APIWeatherService
import com.example.network.utils.GoogleMaps
import com.example.network.utils.OpenMeteoBaseUrl
import com.example.network.utils.Weather
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
    @Weather
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val callFactory = Call.Factory { request -> okHttpClient.newCall(request) }
        return Retrofit.Builder()
            .baseUrl(OpenMeteoBaseUrl.OPEN_METEO_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .callFactory(callFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(@Weather retrofit: Retrofit) =
        retrofit.create(APIWeatherService::class.java)


}
