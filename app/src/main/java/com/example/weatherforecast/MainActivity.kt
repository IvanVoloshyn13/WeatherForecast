package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.data.location.checkLocationPermission
import com.example.weatherforecast.connectivity.ConnectivityNetworkObserver
import com.example.weatherforecast.connectivity.ConnectivityStatus
import com.example.weatherforecast.connectivity.GpsStatus
import com.example.weatherforecast.connectivity.GpsStatusBroadcastReceiver
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.connectivity.toGpsStatus
import com.example.weatherforecast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GpsStatusBroadcastReceiver.GpsStatusListener,
    UpdateConnectivityStatus {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var gpsStatusBroadcastReceiver: GpsStatusBroadcastReceiver
    private lateinit var connectivityNetworkObserver: ConnectivityNetworkObserver
    private lateinit var scope: CoroutineScope
    private var _gpsStatus: GpsStatus = GpsStatus.Available

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!checkLocationPermission()) {
            requestLocationPermission()
        }
        gpsStatusBroadcastReceiver = GpsStatusBroadcastReceiver(
            this as GpsStatusBroadcastReceiver.GpsStatusListener,
            this
        )
        connectivityNetworkObserver = ConnectivityNetworkObserver(this)
        scope = CoroutineScope(Dispatchers.Main)
        if (savedInstanceState == null) {
            val gpsStatus = checkGpsStatus()
            receiveGpsStatus(gpsStatus)
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

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(gpsStatusBroadcastReceiver, filter)
    }

    override fun onResume() {
        super.onResume()
        scope.launch {
            connectivityNetworkObserver.observe().collectLatest { network ->
                networkStatus.update {
                    it.copy(networkStatus = network)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(GPS_STATUS_KEY, _gpsStatus)
    }

    @Suppress("DEPRECATION")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            val gpsStatus =
                savedInstanceState.getSerializable(GPS_STATUS_KEY, GpsStatus::class.java)
            if (gpsStatus != null) {
                receiveGpsStatus(gpsStatus)
            }
        } else {
            val gpsStatus = savedInstanceState.getSerializable(GPS_STATUS_KEY)
            if (gpsStatus != null) {
                receiveGpsStatus(gpsStatus as GpsStatus)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(gpsStatusBroadcastReceiver)
        scope.launch {
            networkStatus.update {
                it.copy(networkStatus = null)
            }
        }

    }


//    private fun initNavigation() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
//        val navController = navHostFragment?.findNavController()
//        navController?.navigate(R.id.mainFragment)
//    }


    override fun receiveGpsStatus(gpsStatus: GpsStatus) {
        scope.launch {
            networkStatus.update {
                it.copy(gpsStatus = gpsStatus)
            }
        }
        _gpsStatus = gpsStatus
    }

    override val networkStatus: MutableStateFlow<ConnectivityStatus> =
        MutableStateFlow(ConnectivityStatus.DEFAULT)

    private fun checkGpsStatus(): GpsStatus {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isProviderEnabled.toGpsStatus()
    }

    companion object {
        const val GPS_STATUS_KEY = "SAVED_GPS"
        const val request_code = 201
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

}






