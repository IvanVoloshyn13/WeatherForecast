package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.CurrentUserLocation
import com.example.domain.models.weather.MainWeatherInfo
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.mainscreen.GetCurrentUserLocationTimeZoneUseCase
import com.example.domain.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.mainscreen.GetLocationTimeUseCase
import com.example.domain.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import com.example.weatherforecast.screens.main.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserLocationUseCase: GetCurrentUserLocationUseCase,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getLocationTimeUseCase: GetLocationTimeUseCase,
    private val getCurrentUserLocationTimeZoneUseCase: GetCurrentUserLocationTimeZoneUseCase
) : ViewModel() {

    private var savedLocationTimeJob: Job? = null


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }

    private val _weather = MutableStateFlow<WeatherComponents?>(null)
    private val _location = MutableStateFlow<CurrentUserLocation>(CurrentUserLocation.DEFAULT)

    private val _time = MutableStateFlow<String?>("0:00")
    val time = _time.asStateFlow()

    private val _mainScreenState = MutableStateFlow<MainScreenState?>(MainScreenState.Default)
    val mainScreenState = _mainScreenState.asStateFlow()

    init {

        getWeatherByCurrentUserLocation()

        combine(_weather, _location, _time) { weather, location, time ->
            _mainScreenState.update { state ->
                state?.copy(
                    city = location.city,
                    mainWeatherInfo = weather?.mainWeatherInfo ?: MainWeatherInfo.Default,
                    hourlyForecast = weather?.hourlyForecast?.get(0),
                    time = time ?: ""
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
                                longitude = currentUserLocation.longitude,
                                timeZoneID = currentUserLocation.timeZoneID
                            )
                        getWeatherByLocation(
                            currentUserLocation.latitude,
                            currentUserLocation.longitude
                        )
                        val timezone = getCurrentUserLocationTimeZoneUseCase.invoke(
                            currentUserLocation.latitude,
                            currentUserLocation.longitude
                        )
                        if(timezone.isNotEmpty()){
                            getLocationTime(timezone)
                        }

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

    private suspend fun getWeatherByLocation(latitude: Double, longitude: Double) {
        val weatherResource = getWeatherDataUseCase.invoke(
            latitude = latitude,
            longitude = longitude
        )
        when (weatherResource) {
            is Resource.Success -> {
                weatherResource.data?.let { weatherData ->
                    val dailyForecast = weatherData.dailyForecast
                    val hourlyForecast = weatherData.hourlyForecast
                    val mainWeatherInfo = weatherData.mainWeatherInfo

//                    _weather.update { weather ->
//                        weather?.copy(
//                            mainWeatherInfo = mainWeatherInfo,
//                            hourlyForecast = hourlyForecast,
//                            dailyForecast = dailyForecast
//                        )
//
//                    }

                    _weather.value = WeatherComponents(
                        mainWeatherInfo = mainWeatherInfo,
                        hourlyForecast = hourlyForecast,
                        dailyForecast = dailyForecast
                    )

                }
            }

            is Resource.Error -> {}
            is Resource.Loading -> {}
        }
    }

    fun getLocationTime(timeZoneId: String) {
        savedLocationTimeJob = viewModelScope.launch {
            getLocationTimeUseCase.invoke(timeZoneId, true).collectLatest {
                _time.value = it
            }
        }
    }


    fun stopTimeObserve() {
        savedLocationTimeJob?.cancel()
    }


}