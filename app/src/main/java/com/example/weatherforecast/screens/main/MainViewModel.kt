package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.mainscreen.location.CurrentUserLocation
import com.example.domain.models.mainscreen.unsplash.CityImage
import com.example.domain.models.mainscreen.weather.MainWeatherInfo
import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationTimeZoneUseCase
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetLocationTimeUseCase
import com.example.domain.usecase.mainscreen.GetUnsplashImageByCityName
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import com.example.weatherforecast.screens.main.models.GetWeather
import com.example.weatherforecast.screens.main.models.GetWeatherByCurrentLocation
import com.example.weatherforecast.screens.main.models.MainScreenEvents
import com.example.weatherforecast.screens.main.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
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
    private val getCurrentUserLocationTimeZoneUseCase: GetCurrentUserLocationTimeZoneUseCase,
    private val getUnsplashImageByCityName: GetUnsplashImageByCityName

) : ViewModel() {

    private var locationTimeJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }

    private val _weather = MutableStateFlow<WeatherComponents?>(null)
    private val _location = MutableStateFlow<CurrentUserLocation>(CurrentUserLocation.DEFAULT)
    private val _time = MutableStateFlow<String?>("0:00")
    private val _cityImage = MutableStateFlow<CityImage?>(value = null)
    private val _mainScreenState = MutableStateFlow<MainScreenState?>(MainScreenState.Default)
    val mainScreenState = _mainScreenState.asStateFlow()


    init {
        combine(
            _weather,
            _location,
            _time,
            _cityImage
        ) { weather, location, time, cityImage ->
            _mainScreenState.update { state ->
                state?.copy(
                    location = location.city,
                    mainWeatherInfo = weather?.mainWeatherInfo ?: MainWeatherInfo.Default,
                    hourlyForecast = weather?.hourlyForecast,
                    dailyForecast = weather?.dailyForecast,
                    time = time ?: "",
                    cityImage = cityImage,
//                    isLoading = location.city.isEmpty() ||
//                        cityImage?.cityImageUrl == null ||
//                        weather?.hourlyForecast == null ||
//                        weather?.dailyForecast == null
                )
            }
        }.launchIn(viewModelScope)

    }

    fun setEvent(event: MainScreenEvents) {
        when (event) {
            GetWeatherByCurrentLocation -> {
                getDataByCurrentUserLocation()
            }

            is GetWeather -> {
                getDataBySearchedCityLocation(event.city)
                viewModelScope.launch {
                }
            }
        }
    }

    private fun getDataByCurrentUserLocation() {
        viewModelScope.launch(exceptionHandler) {
            val locationResource = getCurrentUserLocation()
            if (locationResource != null) {
                val timezoneId =
                    getWeatherByLocation(
                        locationResource.latitude,
                        locationResource.longitude
                    )

                if (locationTimeJob != null) {
                    stopTimeObserve()
                    if (locationTimeJob?.isCompleted == true) {
                        getTimeForLocation(timeZoneId = timezoneId)
                    }
                } else {
                    getTimeForLocation(timeZoneId = timezoneId)
                }
                getCityImage(locationResource.city)

            }
        }
    }


    private fun getDataBySearchedCityLocation(city: SearchedCity) {
        viewModelScope.launch {
            getWeatherByLocation(city.latitude, city.longitude)
            _location.value =
                CurrentUserLocation(
                    city = city.cityName,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    timeZoneID = city.timezone
                )
            getCityImage(city.cityName)
            stopTimeObserve()
            if (locationTimeJob?.isCompleted == true) {
                getTimeForLocation(timeZoneId = city.timezone)
            }

        }
    }

    private suspend fun getCurrentUserLocation(): CurrentUserLocation? {
        val locationResource = currentUserLocationUseCase.invoke()
        return when (locationResource) {
            is Resource.Success -> {
                locationResource.data.let { currentUserLocation ->
                    _location.value =
                        CurrentUserLocation(
                            city = currentUserLocation.city,
                            latitude = currentUserLocation.latitude,
                            longitude = currentUserLocation.longitude,
                            timeZoneID = currentUserLocation.timeZoneID
                        )
                }
                locationResource.data
            }


            is Resource.Error -> {
                // TODO()
                null
            }

        }

    }


    private suspend fun getCityImage(cityName: String) {
        val image = getUnsplashImageByCityName.invoke(cityName)
        when (image) {
            is Resource.Success -> {
                image.data.let {
                    _cityImage.value = it ?: null
                }
            }

            is Resource.Error -> {  // TODO()
            }

        }
    }


    private suspend fun getWeatherByLocation(latitude: Double, longitude: Double): String {
        val weatherResource = getWeatherDataUseCase.invoke(
            latitude = latitude,
            longitude = longitude
        )
        return when (weatherResource) {
            is Resource.Success -> {
                weatherResource.data.let { weatherData ->
                    val dailyForecast = weatherData.dailyForecast
                    val hourlyForecast = weatherData.hourlyForecast
                    val mainWeatherInfo = weatherData.mainWeatherInfo
                    val timeZoneId = weatherData.timezone

                    _weather.value = WeatherComponents(
                        mainWeatherInfo = mainWeatherInfo,
                        hourlyForecast = hourlyForecast,
                        dailyForecast = dailyForecast,
                        timezone = timeZoneId
                    )

                }
                weatherResource.data.timezone
            }

            is Resource.Error -> {
                ""
                // TODO
            }

        }
    }

    private fun getTimeForLocation(timeZoneId: String) {
        locationTimeJob = viewModelScope.launch {
            getLocationTimeUseCase.invoke(timeZoneId, true).cancellable().collectLatest {
                _time.value = it
            }
        }
    }


    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }


}