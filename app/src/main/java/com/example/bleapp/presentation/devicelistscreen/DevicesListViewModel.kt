package com.example.bleapp.presentation.devicelistscreen

import android.bluetooth.le.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bleapp.common.Resource
import com.example.bleapp.services.ScannerService
import com.example.bleapp.ui.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DevicesListViewModel @Inject constructor(
    private val scannerService: ScannerService,
    private val router: Router
) : ViewModel() {

    private val _uiState: MutableLiveData<DeviceListState> = MutableLiveData<DeviceListState>()
    val uiState: LiveData<DeviceListState> = _uiState

    fun startScan() {
        viewModelScope.launch {
            scannerService.observeScan().collect { resource ->
                when (resource) {
                    is Resource.Success -> _uiState.value =
                        DeviceListState(scanResults = resource.data)
                    is Resource.Loading -> _uiState.value = DeviceListState(isLoading = true)
                    is Resource.Error -> _uiState.value =
                        DeviceListState(
                            error = resource.message ?: "An unexpected error occured"
                        )
                }

            }
        }
    }

    fun onDeviceSelected(scanResult: ScanResult) {
        router.goToDeviceDetail(scanResult)
    }


}

