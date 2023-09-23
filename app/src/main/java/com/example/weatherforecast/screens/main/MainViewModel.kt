package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.weather.HourlyForecast
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserLocationUseCase: GetCurrentUserLocationUseCase,
    private val getWeatherDataUseCase: GetWeatherDataUseCase
) : ViewModel() {


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }

    private val _currentWeather = MutableStateFlow<Resource<HourlyForecast>>(Resource.Loading())

    fun getLocation() {
        viewModelScope.launch(exceptionHandler) {
            val result = currentUserLocationUseCase.invoke()
            when (result) {
                is Resource.Success -> {
                    getWeatherDataUseCase.invoke(
                        latitude = result.data!!.latitude,
                        longitude = result.data!!.longitude
                    )
                }

                is Resource.Error -> {
                    Log.d("LOG", result.message.toString())
                }

                else -> {
                    Log.d("LOG", "Some Error")
                }
            }

        }

    }


}