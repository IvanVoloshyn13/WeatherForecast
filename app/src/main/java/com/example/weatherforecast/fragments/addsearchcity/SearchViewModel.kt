package com.example.weatherforecast.fragments.addsearchcity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.SearchedCity
import com.example.domain.usecase.addsearch.SaveCityUseCase
import com.example.domain.usecase.addsearch.SearchCityByNameUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCityByNameUseCase: SearchCityByNameUseCase,
    private val saveCityUseCase: SaveCityUseCase
) : ViewModel() {

    private val _cities: MutableStateFlow<List<SearchedCity>> =
        MutableStateFlow(ArrayList<SearchedCity>())
    val cities = _cities.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun search(query: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            if (query.length >= 3) {
                val list = searchCityByNameUseCase.invoke(query)
                when (list) {
                    // TODO()
                    is Resource.Success -> {
                        _cities.emit(list.data)
                        _isLoading.emit(false)
                    }

                    is Resource.Error -> {
                        Log.d("CITY_ERROR", list.message.toString())
                    }


                }
            } else {
                _cities.emit(ArrayList())
                _isLoading.emit(false)
            }
        }

    }

    fun saveCity(city: SearchedCity) {
        viewModelScope.launch {
            _isLoading.emit(true)
            saveCityUseCase.invoke(city)
            _isLoading.emit(false)
        }
    }
}