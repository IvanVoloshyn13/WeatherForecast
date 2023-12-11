package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.data.location.checkLocationPermission
import com.example.weatherforecast.connectivity.ConnectivityNetworkObserver
import com.example.weatherforecast.connectivity.GpsStatus
import com.example.weatherforecast.connectivity.NetworkStatus
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.connectivity.registerReceiverAsFlow
import com.example.weatherforecast.connectivity.toGpsStatus
import com.example.weatherforecast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    UpdateConnectivityStatus {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var connectivityNetworkObserver: ConnectivityNetworkObserver
    private lateinit var scope: CoroutineScope
    private lateinit var networkJob: Job
    private var initialGpsStatusJob: Job? = null


    override val gpsStatus: MutableSharedFlow<GpsStatus> by lazy {
        MutableSharedFlow(
            replay = 0,
            onBufferOverflow = BufferOverflow.SUSPEND,
            extraBufferCapacity = 0
        )
    }

    override val networkStatus: MutableSharedFlow<NetworkStatus> by lazy {
        MutableSharedFlow(
            replay = 0,
            onBufferOverflow = BufferOverflow.SUSPEND,
            extraBufferCapacity = 0
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        scope = CoroutineScope(Dispatchers.Main)
        connectivityNetworkObserver = ConnectivityNetworkObserver(this@MainActivity)

        if (!checkLocationPermission()) {
            requestLocationPermission()
        }

        if (savedInstanceState == null) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            initialGpsStatusJob = Job()
            scope.launch(initialGpsStatusJob as CompletableJob) {
                gpsStatus.emit(isEnabled.toGpsStatus())
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (requestCode == request_code) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Permission granted $permission")
                } else if (shouldShowRequestPermissionRationale(permission)) {
                    GpsPermissionDialog().show(supportFragmentManager, null)
                    Log.d("PERMISSION", "Permission shouldShowRationale $permission")
                } else {
                    Log.d("PERMISSION", "Permission denied $permission ")
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        initialGpsStatusJob?.invokeOnCompletion {
            initialGpsStatusJob?.cancel()
        }
        networkJob = scope.launch() {
            connectivityNetworkObserver.observe().collectLatest { network ->
                networkStatus.emit(network)
            }
        }

        val gpsStatusFlow =
            registerReceiverAsFlow(
                this,
                IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION),
                scope
            )
        scope.launch {
            gpsStatusFlow.collectLatest {
                gpsStatus.emit(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        networkJob.cancel()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ),
            request_code
        )
    }

    companion object {
        const val request_code = 201
    }

}






