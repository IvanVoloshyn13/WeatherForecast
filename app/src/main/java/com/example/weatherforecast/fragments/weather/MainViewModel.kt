package com.example.weatherforecast.fragments.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.CurrentUserLocation
import com.example.domain.models.SearchedCity
import com.example.domain.models.weather.WeatherComponents
import com.example.domain.usecase.weather.FetchUnsplashImageByCityNameUseCase
import com.example.domain.usecase.weather.FetchWeatherDataUseCase
import com.example.domain.usecase.weather.GetCurrentUserLocationUseCase
import com.example.domain.usecase.weather.GetLocationByIdUseCase
import com.example.domain.usecase.weather.GetLocationTimeUseCase
import com.example.domain.usecase.weather.GetSavedLocationsListUseCase
import com.example.domain.utils.Resource
import com.example.weatherforecast.fragments.weather.models.ErrorState
import com.example.weatherforecast.fragments.weather.models.GetLocationById
import com.example.weatherforecast.fragments.weather.models.GetSavedLocationsList
import com.example.weatherforecast.fragments.weather.models.GetWeather
import com.example.weatherforecast.fragments.weather.models.GetWeatherByCurrentLocation
import com.example.weatherforecast.fragments.weather.models.MainScreenEvents
import com.example.weatherforecast.fragments.weather.models.MainScreenState
import com.example.weatherforecast.fragments.weather.models.ShowLess
import com.example.weatherforecast.fragments.weather.models.ShowMore
import com.example.weatherforecast.fragments.weather.models.ShowMoreLess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val INITIAL_CITIES_LIST_SIZE = 4

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserLocationUseCase: GetCurrentUserLocationUseCase,
    private val fetchWeatherDataUseCase: FetchWeatherDataUseCase,
    private val getLocationTimeUseCase: GetLocationTimeUseCase,
    private val fetchUnsplashImageByCityNameUseCase: FetchUnsplashImageByCityNameUseCase,
    private val getSavedLocationsListUseCase: GetSavedLocationsListUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase
) : ViewModel() {

    private var locationTimeJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }
    private val _mainScreenState =
        MutableStateFlow<MainScreenState>(
            MainScreenState()
        )
    val mainScreenState = _mainScreenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedLocationList()
        }
    }

    fun setEvent(event: MainScreenEvents) {
        when (event) {
            GetWeatherByCurrentLocation -> {
                getDataByCurrentUserLocation()
            }

            is GetWeather -> {
                getDataBySearchedCityLocation(event.city)
            }

            is GetLocationById -> {
                viewModelScope.launch {
                    getLocationById(event.cityId)
                }

            }

            is GetSavedLocationsList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getSavedLocationList()
                }
            }

            is ShowMore -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onShowMoreCitiesPress()
                }
            }

            is ShowLess -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onShowLessCitiesPress()
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
            _mainScreenState.update {
                it.copy(
                    location = city.cityName
                )
            }
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

    private suspend fun getLocationById(cityId: Int) {
        val cityResource = getLocationByIdUseCase.invoke(cityId)
        when (cityResource) {
            is Resource.Success -> {
                getDataBySearchedCityLocation(cityResource.data)
            }

            is Resource.Error -> {}
        }
    }

    private suspend fun getCurrentUserLocation(): CurrentUserLocation? {
        return when (val locationResource = currentUserLocationUseCase.invoke()) {
            is Resource.Success -> {
                locationResource.data.let { currentUserLocation ->
                    _mainScreenState.update {
                        it.copy(
                            location = currentUserLocation.city,
                            gpsProviderError = null
                        )
                    }
                }
                locationResource.data
            }

            is Resource.Error -> {
                locationResource.message?.let { message ->
                    _mainScreenState.update {
                        it.copy(
                            location = CurrentUserLocation.DEFAULT.city,
                            gpsProviderError = ErrorState.GpsProviderError(message = message)
                        )
                    }
                }
                null
            }
        }
    }

    private suspend fun getWeatherByLocation(latitude: Double, longitude: Double): String? {
        val weatherResource = fetchWeatherDataUseCase.invoke(
            latitude = latitude,
            longitude = longitude
        )
        return when (weatherResource) {
            is Resource.Success -> {
                weatherResource.data.let { weatherData ->
                    val dailyForecast = weatherData.dailyForecast
                    val hourlyForecast = weatherData.hourlyForecast
                    val mainWeatherInfo = weatherData.mainWeatherInfo
                    // val timeZoneId = weatherData.timezone
                    _mainScreenState.update {
                        it.copy(
                            mainWeatherInfo = mainWeatherInfo,
                            dailyForecast = dailyForecast,
                            hourlyForecast = hourlyForecast,
                            weatherDataError = null
                        )
                    }
                }
                weatherResource.data.timezone
            }

            is Resource.Error -> {
                weatherResource.message?.let { message ->
                    _mainScreenState.update {
                        it.copy(
                            mainWeatherInfo = WeatherComponents().mainWeatherInfo,
                            dailyForecast = WeatherComponents().dailyForecast,
                            hourlyForecast = WeatherComponents().hourlyForecast,
                            weatherDataError = ErrorState.WeatherDataError(
                                message = message,
                                e = weatherResource.e
                            )
                        )
                    }
                }
                ""
            }
        }
    }

    private suspend fun getCityImage(cityName: String) {
        val cityResource = fetchUnsplashImageByCityNameUseCase.invoke(cityName)
        when (cityResource) {
            is Resource.Success -> {
                if (cityResource.data.cityImageUrl.isNotEmpty()) {
                    _mainScreenState.update {
                        it.copy(
                            currentWeatherLocationImage = cityResource.data.cityImageUrl,
                            imageLoadingError = null
                        )
                    }
                }
            }

            is Resource.Error -> {
                _mainScreenState.update {
                    it.copy(
                        currentWeatherLocationImage = "",
                        imageLoadingError = ErrorState.ImageLoadingError(
                            message = cityResource.message ?: "Cant load data",  //TODO()
                            e = cityResource.e
                        )
                    )
                }
            }
        }
    }

    private suspend fun getSavedLocationList() {
        val citiesResource = getSavedLocationsListUseCase.invoke()
        when (citiesResource) {
            is Resource.Success -> {
                citiesResource.data.let { cities ->
                    if (cities.size > INITIAL_CITIES_LIST_SIZE) {
                        val trimList = cities.dropLast(cities.size - INITIAL_CITIES_LIST_SIZE)
                        _mainScreenState.update {
                            it.copy(
                                cities = trimList as ArrayList<SearchedCity>,
                                showMoreLess = ShowMoreLess.ShowMore
                            )
                        }
                    } else {
                        _mainScreenState.update {
                            it.copy(
                                cities = cities,
                                showMoreLess = ShowMoreLess.Hide
                            )
                        }
                    }
                }
            }

            is Resource.Error -> {
                _mainScreenState.update {
                    it.copy(
                        cities = ArrayList(),
                        showMoreLess = ShowMoreLess.Hide
                    )
                }
            }
        }

    }

    private fun getTimeForLocation(timeZoneId: String) {
        locationTimeJob = viewModelScope.launch {
            getLocationTimeUseCase.invoke(timeZoneId, true).cancellable().collectLatest { time ->
                _mainScreenState.update {
                    it.copy(
                        time = time ?: ""
                    )
                }
            }
        }
    }

    private suspend fun onShowMoreCitiesPress() {
        val citiesResource = getSavedLocationsListUseCase.invoke()
        when (citiesResource) {
            is Resource.Success -> {
                citiesResource.data.let { cities ->
                    _mainScreenState.update {
                        it.copy(cities = cities, showMoreLess = ShowMoreLess.ShowLess)
                    }
                }
            }
            is Resource.Error -> {
                _mainScreenState.update {
                    it.copy(cities = ArrayList(), showMoreLess = ShowMoreLess.Hide)
                }
            }
        }

    }

    private suspend fun onShowLessCitiesPress() {
        getSavedLocationList()
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }

}