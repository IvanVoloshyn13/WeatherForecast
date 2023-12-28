package com.example.weatherforecast

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.weatherforecast.databinding.DialogGpsBinding

class GpsPermissionDialog : DialogFragment() {
    private val binding: DialogGpsBinding by lazy { DialogGpsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bttEnableLocation.setOnClickListener {
            this@GpsPermissionDialog.dialog!!.cancel()
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                201
            )

        }
        binding.bttLater.setOnClickListener {
            dialog!!.cancel()
        }
    }
}