package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.CurrentUserLocation
import com.example.domain.models.weather.MainWeatherInfo
import com.example.domain.models.weather.WeatherData
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
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

    private val _weather = MutableStateFlow<WeatherData?>(null)
    private val _location = MutableStateFlow<CurrentUserLocation?>(null)

    private val _mainScreenState = MutableStateFlow<MainScreenState?>(MainScreenState.Default)
    val mainScreenState = _mainScreenState.asStateFlow()

    init {
        combine(_weather, _location) { weather, location ->
            _mainScreenState.update { state ->
                state?.copy(
                    city = location?.city ?: ""
                )
            }
        }.launchIn(viewModelScope)

    }


    fun getWeatherByCurrentUserLocation() {
        viewModelScope.launch(exceptionHandler) {
            val locationResource = currentUserLocationUseCase.invoke()
            when (locationResource) {
                is Resource.Success -> {
                    locationResource.data?.let { currentUserLocation ->
                        _location.value =
                            CurrentUserLocation(
                                city = currentUserLocation.city,
                                latitude = currentUserLocation.latitude,
                                longitude = currentUserLocation.longitude
                            )

                        getWeatherDataUseCase.invoke(
                            latitude = currentUserLocation.latitude,
                            longitude = currentUserLocation.longitude
                        )
                    }
                }


                is Resource.Error -> {
                    Log.d("ERROR_LOG", locationResource.message.toString())

                }

                else -> {
                    Log.d("LOG", "Some Error")
                }
            }

        }

    }


}