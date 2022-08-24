package com.example.bleapp.model.characteristic

import android.bluetooth.BluetoothGattCharacteristic
import com.example.bleapp.extensions.toHexString
import com.example.bleapp.model.CharacteristicReadingInterface

class CommonCharacteristic(
    private val characteristic: BluetoothGattCharacteristic,
) : BluetoothGattCharacteristic(
    characteristic.uuid,
    characteristic.properties,
    characteristic.permissions
), CharacteristicReadingInterface<String> {
    override fun getParsedValue(): String {
        return if (characteristic.value != null) characteristic.value.toHexString() else ""
    }
}