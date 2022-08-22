package com.example.bleapp.presentation.devicelistscreen

import android.bluetooth.le.ScanResult

data class DeviceListState(
    val isLoading: Boolean = false,
    val scanResults: List<ScanResult>? = null,
    val error: String = ""
)