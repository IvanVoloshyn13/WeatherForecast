package com.example.weatherforecast.fragments.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.mainscreen.location.CurrentUserLocation
import com.example.domain.models.mainscreen.unsplash.ImageUrl
import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetLocationTimeUseCase
import com.example.domain.usecase.mainscreen.GetUnsplashImageByCityNameUseCase
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import com.example.weatherforecast.fragments.main.models.ErrorState
import com.example.weatherforecast.fragments.main.models.GetWeather
import com.example.weatherforecast.fragments.main.models.GetWeatherByCurrentLocation
import com.example.weatherforecast.fragments.main.models.MainScreenEvents
import com.example.weatherforecast.fragments.main.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserLocationUseCase: GetCurrentUserLocationUseCase,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getLocationTimeUseCase: GetLocationTimeUseCase,
    private val getUnsplashImageByCityNameUseCase: GetUnsplashImageByCityNameUseCase

) : ViewModel() {

    private var locationTimeJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }
    private val _location = MutableStateFlow<CurrentUserLocation>(CurrentUserLocation.DEFAULT)
    private val _weather = MutableStateFlow<WeatherComponents>(WeatherComponents())
    private val _time = MutableStateFlow<String?>("0:00")
    private val _imageForLocation =
        MutableStateFlow<ImageUrl>("")

    private val _imageLoadingError =
        MutableStateFlow<ErrorState.ImageLoadingError?>(null)
    private val _gpsProviderError =
        MutableStateFlow<ErrorState.GpsProviderError?>(null)
    private val _weatherDataError =
        MutableStateFlow<ErrorState.WeatherDataError?>(null)

    private val _mainScreenState =
        MutableStateFlow<MainScreenState>(
            MainScreenState()
        )
    val mainScreenState = _mainScreenState.asStateFlow()

    init {
        updateState()
    }

    fun setEvent(event: MainScreenEvents) {
        when (event) {
            GetWeatherByCurrentLocation -> {
                getDataByCurrentUserLocation()
            }

            is GetWeather -> {
                getDataBySearchedCityLocation(event.city)
            }
        }
    }

    private fun updateState() {
        with(viewModelScope) {
            launch {
                _location.collectLatest { location ->
                    _mainScreenState.update {
                        it.copy(
                            location = location.city
                        )
                    }
                }
            }
            launch {
                _weather.collectLatest { weatherComponents ->
                    _mainScreenState.update {
                        it.copy(
                            mainWeatherInfo = weatherComponents.mainWeatherInfo,
                            dailyForecast = weatherComponents.dailyForecast,
                            hourlyForecast = weatherComponents.hourlyForecast,
                        )
                    }
                }
            }
            launch {
                _time.collectLatest { time ->
                    _mainScreenState.update {
                        it.copy(
                            time = time ?: ""
                        )
                    }
                }
            }
            launch {
                _imageForLocation.collectLatest { imageUrl ->
                    _mainScreenState.update {
                        it.copy(
                            currentWeatherLocationImage = imageUrl
                        )
                    }
                }
            }
            launch {
                _gpsProviderError.collectLatest { error ->
                    _mainScreenState.update {
                        it.copy(
                            gpsProviderError = error
                        )
                    }
                }
            }
            launch {
                _imageLoadingError.collectLatest { error ->
                    _mainScreenState.update {
                        it.copy(
                            imageLoadingError = error
                        )
                    }
                }
            }
            launch {
                _weatherDataError.collectLatest { error ->
                    _mainScreenState.update {
                        it.copy(weatherDataError = error)
                    }
                }
            }
        }
    }

    private fun getDataByCurrentUserLocation() {
        viewModelScope.launch(exceptionHandler) {
            val location = getCurrentUserLocation()
            if (location != null) {
                val timezoneId =
                    getWeatherByLocation(
                        location.latitude,
                        location.longitude
                    ) ?: ""

                if (locationTimeJob != null) {
                    stopTimeObserve()
                    if (locationTimeJob?.isCompleted == true) {
                        getTimeForLocation(timeZoneId = timezoneId)
                    }
                } else {
                    getTimeForLocation(timeZoneId = timezoneId)
                }
                getCityImage(location.city)
            }
        }
    }


    private fun getDataBySearchedCityLocation(city: SearchedCity) {
        viewModelScope.launch {
            _location.emit(
                CurrentUserLocation(
                    latitude = city.latitude,
                    longitude = city.longitude,
                    city = city.cityName,
                    timeZoneID = city.timezone
                )
            )
            getWeatherByLocation(city.latitude, city.longitude)
            getCityImage(city.cityName)
            if (locationTimeJob == null) {
                getTimeForLocation(timeZoneId = city.timezone)
            } else {
                stopTimeObserve()
                if (locationTimeJob?.isCompleted == true) {
                    getTimeForLocation(timeZoneId = city.timezone)
                }
            }
        }
    }

    private suspend fun getCurrentUserLocation(): CurrentUserLocation? {
        return when (val locationResource = currentUserLocationUseCase.invoke()) {
            is Resource.Success -> {
                _gpsProviderError.emit(null)
                locationResource.data.let { currentUserLocation ->
                    _location.emit(
                        CurrentUserLocation(
                            latitude = currentUserLocation.latitude,
                            longitude = currentUserLocation.longitude,
                            city = currentUserLocation.city,
                            timeZoneID = currentUserLocation.timeZoneID
                        )
                    )
                }
                locationResource.data
            }

            is Resource.Error -> {
                _location.emit(CurrentUserLocation.DEFAULT)
                locationResource.message?.let { message ->
                    _gpsProviderError.emit(ErrorState.GpsProviderError(message = message))
                }
                null
            }
        }
    }


    private suspend fun getCityImage(cityName: String) {
        val cityResource = getUnsplashImageByCityNameUseCase.invoke(cityName)
        when (cityResource) {
            is Resource.Success -> {
                _imageLoadingError.emit(null)
                if (cityResource.data.cityImageUrl.isNotEmpty()) {
                    _imageForLocation.emit(cityResource.data.cityImageUrl)
                }
            }

            is Resource.Error -> {
                _imageForLocation.emit("")
                _imageLoadingError.emit(
                    ErrorState.ImageLoadingError(
                        message = cityResource.message ?: "Cant load data",  //TODO()
                        e = cityResource.e
                    )
                )
            }
        }
    }


    private suspend fun getWeatherByLocation(latitude: Double, longitude: Double): String? {
        val weatherResource = getWeatherDataUseCase.invoke(
            latitude = latitude,
            longitude = longitude
        )
        return when (weatherResource) {
            is Resource.Success -> {
                _weatherDataError.emit(null)
                weatherResource.data.let { weatherData ->
                    val dailyForecast = weatherData.dailyForecast
                    val hourlyForecast = weatherData.hourlyForecast
                    val mainWeatherInfo = weatherData.mainWeatherInfo
                    val timeZoneId = weatherData.timezone

                    _weather.emit(
                        WeatherComponents(
                            mainWeatherInfo = mainWeatherInfo,
                            hourlyForecast = hourlyForecast,
                            dailyForecast = dailyForecast,
                            timezone = timeZoneId,
                        )
                    )
                }
                weatherResource.data.timezone
            }

            is Resource.Error -> {
                weatherResource.message?.let { message ->
                    _weatherDataError.value =
                        ErrorState.WeatherDataError(message = message, e = weatherResource.e)
                }
                _weather.value = WeatherComponents()
                ""
            }
        }
    }

    private fun getTimeForLocation(timeZoneId: String) {
        locationTimeJob = viewModelScope.launch {
            getLocationTimeUseCase.invoke(timeZoneId, true).cancellable().collectLatest {
                _time.emit(it)
            }
        }
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }

}