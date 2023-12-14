package com.example.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = CITIES_TABLE_NAME)
data class CityEntity(
    @PrimaryKey
    val id: Int ,
    val cityName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",
    val country: String = ""
)