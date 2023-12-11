package com.example.network.utils

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleMapsApi


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OpenMeteoApi

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnsplashApi