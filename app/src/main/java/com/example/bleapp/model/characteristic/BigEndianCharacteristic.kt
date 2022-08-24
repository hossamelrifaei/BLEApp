package com.example.bleapp.model.characteristic

import android.bluetooth.BluetoothGattCharacteristic
import com.example.bleapp.model.CharacteristicReadingInterface
import java.nio.ByteBuffer

class BigEndianCharacteristic(
    private val characteristic: BluetoothGattCharacteristic,
) : BluetoothGattCharacteristic(
    characteristic.uuid,
    characteristic.properties,
    characteristic.permissions
), CharacteristicReadingInterface<String> {
    override fun getParsedValue(): String {
        return ByteBuffer.wrap(characteristic.value).int.toString()
    }
}