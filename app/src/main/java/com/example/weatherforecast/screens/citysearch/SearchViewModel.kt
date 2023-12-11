package com.example.weatherforecast.screens.citysearch

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _cities = MutableStateFlow(ArrayList<String>())
    val cities = _cities.asStateFlow()

    fun search(query: String) {
        Log.d("SEARCH", query)
    }
}