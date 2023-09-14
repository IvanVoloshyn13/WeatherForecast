package com.example.weatherforecast.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCurrentUserLocationUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val currentUserLocationUseCase: GetCurrentUserLocationUseCase) :
    ViewModel() {

    fun getLocation() {
        viewModelScope.launch {
            val result = currentUserLocationUseCase.invoke()
            when (result) {
                is Resource.Success -> {
                    Log.d("LOG", result.data?.city.toString())
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