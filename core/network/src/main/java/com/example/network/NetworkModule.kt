package com.example.network

import com.squareup.moshi.Moshi
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
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(moshi: Moshi) = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: Lazy<OkHttpClient>
    ): Retrofit {
        val callFactory = Call.Factory { request -> okHttpClient.value.newCall(request) }
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(moshiConverterFactory)
            .callFactory(callFactory)
            .build()
    }
}