package com.example.bleapp.model

interface CharacteristicReadingInterface<T> {
    fun getParsedValue(): T
}