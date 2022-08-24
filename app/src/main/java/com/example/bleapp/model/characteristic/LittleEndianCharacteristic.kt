package com.example.bleapp.model.characteristic

import android.bluetooth.BluetoothGattCharacteristic
import com.example.bleapp.model.CharacteristicReadingInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class LittleEndianCharacteristic(
    private val characteristic: BluetoothGattCharacteristic,
) : BluetoothGattCharacteristic(
    characteristic.uuid,
    characteristic.properties,
    characteristic.permissions
), CharacteristicReadingInterface<String> {
    override fun getParsedValue(): String {
        return ByteBuffer.wrap(characteristic.value).order(ByteOrder.LITTLE_ENDIAN).int.toString()
    }
}