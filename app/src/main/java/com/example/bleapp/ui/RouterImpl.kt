package com.example.bleapp.ui

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bleapp.R
import com.example.bleapp.presentation.devicedetailescreen.DeviceDetailFragment


class RouterImpl : Router {
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(activity: AppCompatActivity, initialScreen: Fragment) {
        init(activity.supportFragmentManager, initialScreen)
    }

    private fun init(fragmentManager: FragmentManager, rootScreen: Fragment) {
        this.fragmentManager = fragmentManager
        this.fragmentManager
        if (fragmentManager.fragments.size == 0) {
            fragmentManager.beginTransaction()
                .replace(R.id.screen_container, rootScreen)
                .commit()
        }
    }

    override fun pop(): Boolean {
        return fragmentManager.popBackStackImmediate()
    }

    override fun goToDeviceDetail(scanResult: ScanResult) {
        val fragment = DeviceDetailFragment()
        fragment.arguments = bundleOf(BluetoothDevice.EXTRA_DEVICE to scanResult)
        fragmentManager
            .beginTransaction()
            .replace(R.id.screen_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}