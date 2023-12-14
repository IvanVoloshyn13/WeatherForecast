package com.example.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewCity(city: CityEntity): Long

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>


}