package com.example.bleapp.presentation.devicedetailescreen

import android.bluetooth.BluetoothGattCharacteristic

data class DeviceDetailState(
    val isLoading: Boolean = false,
    val bluetoothGattCharacteristics: MutableList<BluetoothGattCharacteristic>? = null,
    val error: String = "",
    val disconnected: Boolean = false
)