package com.example.domain.models.searchscreen

data class SearchedCity(
    val cityName:String,
    val latitude:Double,
    val longitude:Double,
    val timezone:String,
    val country:String
)
