package com.example.bleapp.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import com.example.bleapp.R
import com.example.bleapp.base.BaseActivity
import com.example.bleapp.extensions.hasPermission
import com.example.bleapp.extensions.requestPermission
import com.example.bleapp.presentation.devicelistscreen.DevicesListFragment
import dagger.hilt.android.AndroidEntryPoint


private const val LOCATION_PERMISSION_REQUEST_CODE = 2

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)


    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initialScreen(): Fragment {
        return DevicesListFragment()
    }


    override fun onResume() {
        super.onResume()
        if (!isLocationPermissionGranted) {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    requestPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }
}