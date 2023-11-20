package com.example.weatherforecast

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.utils.GpsStatus
import com.example.weatherforecast.utils.GpsStatusBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GpsStatusBroadcastReceiver.GpsStatusListener,
    UpdateGpsStatus {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var gpsStatusBroadcastReceiver: GpsStatusBroadcastReceiver
    private lateinit var scope:CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        gpsStatusBroadcastReceiver = GpsStatusBroadcastReceiver(this, applicationContext)
        scope= CoroutineScope(Dispatchers.IO)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ),
            request_code
        )

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
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        this.registerReceiver(gpsStatusBroadcastReceiver, filter)
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(gpsStatusBroadcastReceiver)
    }

//    private fun initNavigation() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
//        val navController = navHostFragment?.findNavController()
//        navController?.navigate(R.id.mainFragment)
//    }

    companion object {
        const val request_code = 201
    }

    override fun receiveGpsStatus(gpsStatus: GpsStatus) {
        scope.launch {
            gpsStatusFlow.emit(gpsStatus)
        }
    }


    override val gpsStatusFlow: MutableSharedFlow<GpsStatus> =
        MutableSharedFlow(onBufferOverflow = BufferOverflow.SUSPEND, replay = 0)


}


