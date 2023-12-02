package com.example.weatherforecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.data.location.checkLocationPermission
import com.example.weatherforecast.connectivity.ConnectivityNetworkObserver
import com.example.weatherforecast.connectivity.ConnectivityStatus
import com.example.weatherforecast.connectivity.GpsStatusBroadcastLiveData
import com.example.weatherforecast.connectivity.UpdateConnectivityStatus
import com.example.weatherforecast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    UpdateConnectivityStatus {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var connectivityNetworkObserver: ConnectivityNetworkObserver
    private lateinit var scope: CoroutineScope

    lateinit var gpsStatusBroadcastLiveData: GpsStatusBroadcastLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        gpsStatusBroadcastLiveData = GpsStatusBroadcastLiveData(this)

        if (!checkLocationPermission()) {
            requestLocationPermission()
        }
        connectivityNetworkObserver = ConnectivityNetworkObserver(this)
        scope = CoroutineScope(Dispatchers.Main)

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
        scope.launch {
            connectivityNetworkObserver.observe().collectLatest { network ->
                networkStatus.update {
                    it.copy(networkStatus = network)
                }
            }
        }
        gpsStatusBroadcastLiveData.observe(this) { gpsStatus ->
            networkStatus.update {
                it.copy(gpsStatus = gpsStatus)
            }

        }
    }


    override fun onPause() {
        super.onPause()
        scope.launch {
            networkStatus.update {
                it.copy(networkStatus = null)
            }
        }
        gpsStatusBroadcastLiveData.removeObservers(this)
    }


//    private fun initNavigation() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
//        val navController = navHostFragment?.findNavController()
//        navController?.navigate(R.id.mainFragment)
//    }


    override val networkStatus: MutableStateFlow<ConnectivityStatus> =
        MutableStateFlow(ConnectivityStatus.DEFAULT)


    companion object {
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






