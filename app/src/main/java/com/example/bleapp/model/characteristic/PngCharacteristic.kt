package com.example.bleapp.model.characteristic

import android.bluetooth.BluetoothGattCharacteristic
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.bleapp.model.CharacteristicReadingInterface

class PngCharacteristic(
    private val characteristic: BluetoothGattCharacteristic,
) : BluetoothGattCharacteristic(
    characteristic.uuid,
    characteristic.properties,
    characteristic.permissions
), CharacteristicReadingInterface<Bitmap> {
    override fun getParsedValue(): Bitmap {
        return BitmapFactory.decodeByteArray(
            characteristic.value,
            0,
            characteristic.value.size
        )
    }
}