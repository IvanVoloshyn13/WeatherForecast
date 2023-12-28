package com.example.domain.models.searchscreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchedCity(
    val id: Int,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val country: String

) : Parcelable {

    override fun toString(): String {
        return "$cityName, $country"
    }


}
