package com.example.bleapp.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.example.bleapp.common.Resource
import com.example.bleapp.extensions.isNotifiable
import com.example.bleapp.extensions.isReadable
import com.example.bleapp.extensions.listenToCharacteristicNotification
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConnectionManager @Inject constructor() {

    var bluetoothGatt: BluetoothGatt? = null
    private lateinit var gattCallback: BluetoothGattCallback
    private var isWorking = false

    fun observeConnection(
        context: Context,
        device: BluetoothDevice
    ): Flow<Resource<BluetoothGattCharacteristic>> = callbackFlow {
        trySendBlocking(Resource.Loading())
        gattCallback = object : BluetoothGattCallback() {

            var chars: MutableList<BluetoothGattCharacteristic> = mutableListOf()

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                characteristic?.value?.let {
                    trySendBlocking(Resource.Success(characteristic))
                }
                chars.removeFirst()
                writeDescriptors(gatt)
            }


            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        gatt?.discoverServices()
                    }
                    if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        gatt?.close()
                        trySendBlocking(Resource.Disconnected())
                        isWorking = false

                    }
                    if (newState == BluetoothProfile.STATE_CONNECTING) {
                        isWorking = true
                    }
                } else {
                    gatt?.close()
                    trySendBlocking(Resource.Disconnected())
                    isWorking = false

                }
            }


            override fun onDescriptorWrite(
                gatt: BluetoothGatt?,
                descriptor: BluetoothGattDescriptor?,
                status: Int
            ) {
                chars.removeFirst()
                writeDescriptors(gatt);
            }


            @SuppressLint("SetTextI18n")
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                characteristic?.value?.let {
                    trySendBlocking(Resource.Success(characteristic))
                }
            }


            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    gatt?.services?.forEach { service ->
                        service.characteristics.forEach { characteristics ->
                            chars.add(characteristics)
                        }
                    }

                    writeDescriptors(gatt)
                }
            }

            private fun writeDescriptors(gatt: BluetoothGatt?) {
                if (chars.isNotEmpty()) {
                    val characteristic = chars.first()
                    if (characteristic.isNotifiable()) {
                        gatt?.listenToCharacteristicNotification(characteristic)
                    } else if (characteristic.isReadable() && !characteristic.isNotifiable()) {
                        gatt?.readCharacteristic(characteristic)
                    } else {
                        chars.removeFirst()
                        writeDescriptors(gatt)
                    }
                }
            }
        }
        if (isWorking.not()) {
            bluetoothGatt = device.connectGatt(
                context,
                false,
                gattCallback
            )
        }
        awaitClose { }
    }

    fun disconnect(): Flow<Resource<BluetoothGattCharacteristic>> = flow {
        emit(Resource.Loading())
        bluetoothGatt?.disconnect()
    }

}