package com.example.bleapp.ui

import android.bluetooth.le.ScanResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface Router {
    fun onCreate(activity: AppCompatActivity, initialScreen: Fragment)
    fun pop(): Boolean
    fun goToDeviceDetail(scanResult: ScanResult)
}