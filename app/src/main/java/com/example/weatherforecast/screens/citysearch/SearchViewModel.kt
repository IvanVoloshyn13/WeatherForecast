package com.example.weatherforecast.screens.citysearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.searchscreen.SearchedCity
import com.example.domain.usecase.searchscreen.SearchCityByNameUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCityByNameUseCase: SearchCityByNameUseCase
) : ViewModel() {

    private val _cities: MutableStateFlow<ArrayList<SearchedCity>> =
        MutableStateFlow(ArrayList<SearchedCity>())
    val cities = _cities.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            if (query.length >= 3) {
                val list = searchCityByNameUseCase.invoke(query)
                when (list) {
                    // TODO()
                    is Resource.Success -> {
                        _cities.emit(list.data)
                    }

                    is Resource.Error -> {
                        Log.d("CITY_ERROR", list.message.toString())
                    }
                        is Resource.Loading -> {}

                    }
                } else {
                    _cities.emit(ArrayList())
                }
            }


        }
    }