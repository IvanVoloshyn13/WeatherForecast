package com.example.http.interceptors.connectivity

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Suppress("DEPRECATION")
class ConnectivityInterceptor @Inject constructor(@ApplicationContext context: Context) :
    Interceptor {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!
    override fun intercept(chain: Interceptor.Chain): Response {
        val connected = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
        if (!connected) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }
}