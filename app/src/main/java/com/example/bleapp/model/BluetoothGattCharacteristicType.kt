package com.example.bleapp.model

import com.example.bleapp.model.BluetoothConstants.BIG_ENDIAN_UUID
import com.example.bleapp.model.BluetoothConstants.LITTLE_ENDIAN_UUID
import com.example.bleapp.model.BluetoothConstants.PNG_UUID
import com.example.bleapp.model.BluetoothConstants.STRING_UUID

enum class BluetoothGattCharacteristicType(val uuid: String) {
    LittleEndianCharacteristicType(LITTLE_ENDIAN_UUID),
    BigEndianCharacteristicType(BIG_ENDIAN_UUID),
    StringCharacteristicType(STRING_UUID),
    PngCharacteristicType(PNG_UUID)
}
