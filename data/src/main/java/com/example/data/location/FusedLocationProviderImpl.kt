package com.example.data.location


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.domain.location.FusedLocationProvider
import com.example.domain.models.CurrentUserLocation
import com.example.domain.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FusedLocationProviderImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext val context: Context

) : FusedLocationProvider {
    override suspend fun getCurrentUserLocation(
    ): Resource<CurrentUserLocation> {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasPermission = (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return if (!hasPermission || !isGpsEnabled || isNetworkEnabled) {

            Resource.Error(message = "")
        } else {

            Resource.Success(CurrentUserLocation(43, 43, ""))
        }





    }
}