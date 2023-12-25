package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.mainscreen.location.CurrentUserLocation
import com.example.domain.models.mainscreen.unsplash.CityImage
import com.example.domain.models.mainscreen.weather.MainWeatherInfo
import com.example.domain.models.mainscreen.weather.WeatherComponents
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.usecase.mainscreen.GetCurrentUserLocationUseCase
import com.example.domain.usecase.mainscreen.GetLocationTimeUseCase
import com.example.domain.usecase.mainscreen.GetUnsplashImageByCityName
import com.example.domain.usecase.mainscreen.GetWeatherDataUseCase
import com.example.domain.utils.Resource
import com.example.weatherforecast.screens.main.models.ErrorState
import com.example.weatherforecast.screens.main.models.GetWeather
import com.example.weatherforecast.screens.main.models.GetWeatherByCurrentLocation
import com.example.weatherforecast.screens.main.models.MainErrorState
import com.example.weatherforecast.screens.main.models.MainScreenEvents
import com.example.weatherforecast.screens.main.models.MainScreenState
import com.example.weatherforecast.screens.main.models.SuccessState
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
    private val getUnsplashImageByCityName: GetUnsplashImageByCityName

) : ViewModel() {

    private var locationTimeJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }

    private val _weather = MutableStateFlow<WeatherComponents?>(null)
    private val _location = MutableStateFlow<CurrentUserLocation>(CurrentUserLocation.DEFAULT)
    private val _time = MutableStateFlow<String?>("0:00")
    private val _cityImage = MutableStateFlow<CityImage>(CityImage(cityImageUrl = ""))
    private val _successState = MutableStateFlow<SuccessState?>(SuccessState.Default)
    val successState = _successState.asStateFlow()

    private val _imageLoadingError = MutableStateFlow<ErrorState.ImageLoadingError?>(null)
    private val _gpsProviderError = MutableStateFlow<ErrorState.GpsProviderError?>(null)
    private val _weatherDataError = MutableStateFlow<ErrorState.WeatherDataError?>(null)
    private val _errorState = MutableStateFlow<MainErrorState>(
        MainErrorState(
            gpsProviderError = null,
            imageLoadingError = null,
            weatherDataError = null
        )
    )

    private val _mainScreenState =
        MutableStateFlow<MainScreenState>(
            MainScreenState(
                successState = SuccessState.Default,
                errorState = MainErrorState()
            )
        )

    val mainScreenState = _mainScreenState.asStateFlow()


    init {

        combine(
            _weather,
            _location,
            _time,
            _cityImage,
            ::combineSuccessState
        ).launchIn(viewModelScope)

        combine(
            _imageLoadingError,
            _weatherDataError,
            _gpsProviderError,
            ::combineErrorState
        ).launchIn(viewModelScope)

        combine(
            _successState, _errorState
        ) { successState, errorState ->
            _mainScreenState.update {
                it.copy(
                    successState = successState ?: SuccessState.Default,
                    errorState = errorState
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

            } else {
//TODO()
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
        val locationResource = currentUserLocationUseCase.invoke()
        return when (locationResource) {
            is Resource.Success -> {
                locationResource.data.let { currentUserLocation ->
                    _gpsProviderError.value = null
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
                locationResource.message?.let { message ->
                    _gpsProviderError.value = ErrorState.GpsProviderError(message = message)
                }
                _location.value = CurrentUserLocation.DEFAULT
                null
            }
        }
    }


    private suspend fun getCityImage(cityName: String) {
        val image = getUnsplashImageByCityName.invoke(cityName)
        when (image) {
            is Resource.Success -> {
                image.data.let {
                    _cityImage.value = it
                }
                _imageLoadingError.value = null
            }

            is Resource.Error -> {
                _cityImage.value = CityImage(cityImageUrl = null)
                image.message?.let { message ->
                    _imageLoadingError.value =
                        ErrorState.ImageLoadingError(message = message, e = image.e)
                }
                _cityImage.value = CityImage(cityImageUrl = null)
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
                _weather.value = null
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
                weatherResource.message?.let { message ->
                    _weatherDataError.value =
                        ErrorState.WeatherDataError(message = message, e = weatherResource.e)
                }
                _weather.value = null
                ""

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

    private fun combineSuccessState(
        weather: WeatherComponents?,
        location: CurrentUserLocation,
        time: String?,
        cityImage: CityImage?
    ) {
        _successState.update { state ->
            state?.copy(
                location = location.city,
                mainWeatherInfo = weather?.mainWeatherInfo ?: MainWeatherInfo.Default,
                hourlyForecast = weather?.hourlyForecast,
                dailyForecast = weather?.dailyForecast,
                time = time ?: "",
                cityImage = cityImage,
            )
        }
    }

    private fun combineErrorState(
        imageError: ErrorState.ImageLoadingError?,
        weatherDataError: ErrorState.WeatherDataError?,
        gpsProviderError: ErrorState.GpsProviderError?
    ) {
        _errorState.update { state ->
            state.copy(
                gpsProviderError = gpsProviderError,
                weatherDataError = weatherDataError,
                imageLoadingError = imageError
            )

        }
    }

}