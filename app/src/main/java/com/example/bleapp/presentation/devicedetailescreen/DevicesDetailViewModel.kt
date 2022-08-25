package com.example.bleapp.presentation.devicedetailescreen

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanResult
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bleapp.common.Resource
import com.example.bleapp.model.CharacteristicFactory
import com.example.bleapp.services.ConnectionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val characteristicFactory = CharacteristicFactory()
    private val charsList = mutableListOf<BluetoothGattCharacteristic>()
    private val _deviceState = MutableLiveData<ScanResult>()
    val deviceState: LiveData<ScanResult> = _deviceState

    private val _servicesState = MutableLiveData<DeviceDetailState>()
    val servicesState: LiveData<DeviceDetailState> = _servicesState

    init {
        savedStateHandle.get<ScanResult>(BluetoothDevice.EXTRA_DEVICE)?.let { scanResult ->
            _deviceState.value = scanResult

            subscribeToObservers()
        }
    }

    private fun subscribeToObservers() {


        viewModelScope.launch {
            ConnectionService.connectionResource.collect { resource ->
                when (resource) {
                    is Resource.Success -> {

                        resource.data?.let { newChar ->
                            if (characteristicFactory.isBitmap(newChar)) {
                                characteristicFactory.createCharacteristic<Bitmap>(newChar)
                            } else {
                                characteristicFactory.createCharacteristic<String>(newChar)
                            }

                            _servicesState.value =
                                DeviceDetailState(bluetoothGattCharacteristic = newChar)

                        }


                    }
                    is Resource.Loading -> _servicesState.value =
                        DeviceDetailState(isLoading = true)
                    is Resource.Disconnected -> _servicesState.value = DeviceDetailState(
                        disconnected = true
                    )
                }

            }
        }
    }
}

