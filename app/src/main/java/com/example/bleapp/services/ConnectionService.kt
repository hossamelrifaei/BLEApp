package com.example.bleapp.services

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.bleapp.common.Constants.ACTION_DISCONNECT_SERVICE
import com.example.bleapp.common.Constants.ACTION_START_SERVICE
import com.example.bleapp.common.Constants.ACTION_STOP_SERVICE
import com.example.bleapp.common.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : LifecycleService() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    private lateinit var device: BluetoothDevice

    companion object {
        val connectionResource: MutableStateFlow<Resource<BluetoothGattCharacteristic>> =
            MutableStateFlow(Resource.Initial())
    }

    override fun onDestroy() {
        connectionResource.value = Resource.Initial()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        ?: error("Missing BluetoothDevice")
                    startConnection()
                }

                ACTION_DISCONNECT_SERVICE -> {
                    lifecycleScope.launch {
                        connectionManager.disconnect()
                            .collect { resource ->
                                connectionResource.value = resource
                            }
                    }
                }

                ACTION_STOP_SERVICE -> {
                    stopSelf()
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startConnection() {
        lifecycleScope.launch {
            connectionManager.observeConnection(this@ConnectionService, device)
                .collect { resource ->
                    connectionResource.value = resource
                }
        }
    }


}