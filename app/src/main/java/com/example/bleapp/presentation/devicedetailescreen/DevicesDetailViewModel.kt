package com.example.bleapp.presentation.devicedetailescreen

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state = MutableLiveData<ScanResult>()
    val state: LiveData<ScanResult> = _state

    init {
        savedStateHandle.get<ScanResult>(BluetoothDevice.EXTRA_DEVICE)?.let { scanResult ->
            _state.value = scanResult
        }
    }
}