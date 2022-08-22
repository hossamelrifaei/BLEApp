package com.example.bleapp.presentation.devicelistscreen

import android.bluetooth.le.ScanResult
import androidx.recyclerview.widget.DiffUtil

class ScanResultsCallBack(
    private val oldList: List<ScanResult>,
    private val newList: List<ScanResult>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].device.address == newList[newItemPosition].device.address
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].rssi == newList[newItemPosition].rssi
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return newList[newItemPosition]
    }
}