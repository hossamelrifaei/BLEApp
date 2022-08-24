package com.example.bleapp.extensions

import java.nio.charset.StandardCharsets

fun ByteArray.covertToUTF8String(): String =
    String(this, StandardCharsets.UTF_8)


fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }