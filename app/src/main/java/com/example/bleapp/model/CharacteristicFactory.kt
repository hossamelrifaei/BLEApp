package com.example.bleapp.model

import android.bluetooth.BluetoothGattCharacteristic
import com.example.bleapp.model.characteristic.BigEndianCharacteristic
import com.example.bleapp.model.characteristic.CommonCharacteristic
import com.example.bleapp.model.characteristic.LittleEndianCharacteristic
import com.example.bleapp.model.characteristic.PngCharacteristic
import com.example.bleapp.model.characteristic.StringCharacteristic

class CharacteristicFactory {
    fun <T : Any> createCharacteristic(characteristic: BluetoothGattCharacteristic): CharacteristicReadingInterface<T> {
        return when (characteristic.uuid.toString()) {
            BluetoothGattCharacteristicType.LittleEndianCharacteristicType.uuid -> LittleEndianCharacteristic(
                characteristic
            ) as CharacteristicReadingInterface<T>

            BluetoothGattCharacteristicType.BigEndianCharacteristicType.uuid -> BigEndianCharacteristic(
                characteristic
            ) as CharacteristicReadingInterface<T>
            BluetoothGattCharacteristicType.StringCharacteristicType.uuid -> StringCharacteristic(
                characteristic
            ) as CharacteristicReadingInterface<T>
            BluetoothGattCharacteristicType.PngCharacteristicType.uuid -> PngCharacteristic(
                characteristic
            ) as CharacteristicReadingInterface<T>
            else -> CommonCharacteristic(characteristic) as CharacteristicReadingInterface<T>

        }
    }

    fun isBitmap(characteristic: BluetoothGattCharacteristic): Boolean {
        return when (characteristic.uuid.toString()) {
            BluetoothGattCharacteristicType.PngCharacteristicType.uuid -> true
            else -> false
        }
    }
}