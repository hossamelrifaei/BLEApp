package com.example.bleapp.services

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.example.bleapp.common.Resource
import com.example.bleapp.model.BluetoothConstants.SERVICE_OF_INTEREST_UUID
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ScannerService(@ActivityContext context: Context) {

    private val scanResults = mutableListOf<ScanResult>()
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    //use to get only one device
    private val filter = ScanFilter.Builder().setServiceUuid(
        ParcelUuid.fromString(SERVICE_OF_INTEREST_UUID)
    ).build()

    private val scanSettings: ScanSettings by lazy {
        ScanSettings.Builder()
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            .setReportDelay(0)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    }

    private lateinit var scanCallback: ScanCallback


    fun observeScan(): Flow<Resource<List<ScanResult>>> = callbackFlow {
        trySendBlocking(Resource.Loading())
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val indexQuery =
                    scanResults.indexOfFirst { it.device.address == result.device.address }
                if (indexQuery != -1) {
                    scanResults[indexQuery] = result
                } else {
                    scanResults.add(result)


                }
                trySendBlocking(Resource.Success(scanResults))
            }

            override fun onScanFailed(errorCode: Int) {
                trySendBlocking(Resource.Error("Error scanning devices"))
            }
        }

        scanResults.clear()
        bleScanner.startScan(listOf(), scanSettings, scanCallback)
        awaitClose { }
    }
}