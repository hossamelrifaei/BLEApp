package com.example.bleapp.extensions

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.example.bleapp.model.BluetoothConstants
import java.util.*

fun BluetoothGattCharacteristic.isReadable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)

fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean =
    properties and property != 0

@SuppressLint("MissingPermission")
fun BluetoothGatt.listenToCharacteristicNotification(characteristic: BluetoothGattCharacteristic) {
    this.setCharacteristicNotification(characteristic, true)
    val uuid = UUID.fromString(BluetoothConstants.DESCRIPTOR_UUID)
    val descriptor = characteristic.getDescriptor(uuid)
    if (descriptor != null) {
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        this.writeDescriptor(descriptor)
    }
}

fun BluetoothGattCharacteristic.addOrReplace(charsList: MutableList<BluetoothGattCharacteristic>): MutableList<BluetoothGattCharacteristic> {
    val index = charsList.indexOfFirst { this.uuid == it.uuid }
    if (index == -1) {
        charsList.add(this)
    } else {
        charsList[index] = this
    }
    return charsList
}