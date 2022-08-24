package com.example.bleapp.model.characteristic

import android.bluetooth.BluetoothGattCharacteristic
import com.example.bleapp.extensions.covertToUTF8String
import com.example.bleapp.model.CharacteristicReadingInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class StringCharacteristic(
    private val characteristic: BluetoothGattCharacteristic,
) : BluetoothGattCharacteristic(
    characteristic.uuid,
    characteristic.properties,
    characteristic.permissions
), CharacteristicReadingInterface<String> {
    override fun getParsedValue(): String {
        val firstLength = ByteBuffer
            .wrap(
                characteristic.value.slice(IntRange(0, 2))
                    .toByteArray()
            )
            .order(ByteOrder.LITTLE_ENDIAN).short

        val firstString = characteristic.value.slice(IntRange(2, 2 + firstLength)).toByteArray()
            .covertToUTF8String()

        val secondLength = ByteBuffer.wrap(
            characteristic.value.slice(IntRange(2 + firstLength, 4 + firstLength)).toByteArray()
        ).order(ByteOrder.LITTLE_ENDIAN).short

        val secondString =
            characteristic.value.slice(IntRange(4 + firstLength, 3 + firstLength + secondLength))
                .toByteArray().covertToUTF8String()
        return "$firstString$secondString"
    }
}