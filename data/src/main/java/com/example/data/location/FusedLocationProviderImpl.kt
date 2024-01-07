package com.example.data.location


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.domain.location.FusedLocationProvider
import com.example.domain.models.CurrentUserLocation
import com.example.domain.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FusedLocationProviderImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext val context: Context

) : FusedLocationProvider {
    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getCurrentUserLocation(
    ): Resource<CurrentUserLocation> {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasPermission = context.checkLocationPermission()

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!hasPermission) {
            return Resource.Error(message = "Check permission")
        } else if (!isGpsEnabled || !isNetworkEnabled) {
            return Resource.Error(message = "Check Gps or Network settings")
        }
        return suspendCancellableCoroutine { continuation ->
            var currentUserLocation: CurrentUserLocation = CurrentUserLocation.DEFAULT
            val geocoder = Geocoder(context, Locale.getDefault())
            var latitude: Double
            var longitude: Double
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { locationNullable ->
                locationNullable?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(
                            latitude,
                            longitude,
                            1
                        ) {
                            if (it.size > 0) {
                                currentUserLocation = CurrentUserLocation(
                                    latitude = it[0].latitude,
                                    longitude = it[0].longitude,
                                    city = it[0].locality,
                                    timeZoneID = ""
                                )
                                continuation.resume(Resource.Success(data = currentUserLocation)) { exception ->
                                    continuation.resumeWithException(exception)
                                }
                            }
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        val address = geocoder.getFromLocation(latitude, longitude, 1)
                        if (!address.isNullOrEmpty()) {
                            currentUserLocation = CurrentUserLocation(
                                latitude = latitude,
                                longitude = longitude,
                                city = address[0].locality,
                                timeZoneID = ""
                            )
                            continuation.resume(Resource.Success(data = currentUserLocation)) {
                                continuation.resumeWithException(it)
                            }
                        }
                    }
                }
            }
                .addOnCanceledListener {
                    continuation.resume(Resource.Error(message = "Gps cancelling"))
                }
                .addOnFailureListener {
                    continuation.resume(Resource.Error(message = it.message))
                }
        }
    }
}

fun Context.checkLocationPermission(): Boolean {
    val hasPermission = (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)
    return hasPermission
}