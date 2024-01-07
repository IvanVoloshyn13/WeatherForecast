package com.example.domain.models

data class SearchedCity(
    val id: Int,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val country: String
)  {

    override fun toString(): String {
        return "$cityName, $country"
    }


}
