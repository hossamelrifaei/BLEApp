package com.example.bleapp.presentation.devicedetailescreen

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bleapp.R
import com.example.bleapp.common.Constants.ACTION_DISCONNECT_SERVICE
import com.example.bleapp.common.Constants.ACTION_START_SERVICE
import com.example.bleapp.common.Constants.ACTION_STOP_SERVICE
import com.example.bleapp.services.ConnectionService
import com.example.bleapp.ui.Router
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_devices_detail.btn_connect
import kotlinx.android.synthetic.main.fragment_devices_detail.chars_list
import kotlinx.android.synthetic.main.fragment_devices_detail.tv_device_address
import kotlinx.android.synthetic.main.fragment_devices_detail.tv_device_name
import javax.inject.Inject


@AndroidEntryPoint
class DeviceDetailFragment : Fragment() {
    private val viewModel by viewModels<DevicesDetailViewModel>()
    private var isConnected = false
    private val characteristicAdapter = CharacteristicAdapter(mutableListOf())

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_devices_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chars_list.adapter = characteristicAdapter
        viewModel.deviceState.observe(viewLifecycleOwner) { scanResult ->

            tv_device_name.text = scanResult.scanRecord?.deviceName ?: "N/A"
            tv_device_address.text = scanResult.device?.address
            btn_connect.text = getString(R.string.btn_connect_string)

            btn_connect.setOnClickListener {
                if (isConnected.not()) {
                    startServiceWithParcelableAndAction(
                        ConnectionService::class.java,
                        BluetoothDevice.EXTRA_DEVICE,
                        scanResult.device,
                        ACTION_START_SERVICE
                    )
                } else {
                    startServiceWithParcelableAndAction(
                        ConnectionService::class.java,
                        BluetoothDevice.EXTRA_DEVICE,
                        scanResult.device,
                        ACTION_DISCONNECT_SERVICE
                    )
                }
            }
        }

        viewModel.servicesState.observe(viewLifecycleOwner) { deviceDetailState ->

            if (deviceDetailState.isLoading) {
                btn_connect.text = getString(R.string.btn_connecting_string)
                btn_connect.isEnabled = false
            }

            if (deviceDetailState.disconnected) {
                router.pop()
                startServiceWithParcelableAndAction(
                    ConnectionService::class.java,
                    BluetoothDevice.EXTRA_DEVICE,
                    null,
                    ACTION_STOP_SERVICE
                )
            }

            deviceDetailState.bluetoothGattCharacteristics?.let {
                btn_connect.text = getString(R.string.btn_disconnect_string)
                btn_connect.isEnabled = true
                characteristicAdapter.updateList(it)
                isConnected = true
            }
        }
    }

    private fun startServiceWithParcelableAndAction(
        dest: Class<ConnectionService>,
        extraName: String,
        extra: BluetoothDevice?,
        action: String,
    ) {
        Intent(context, dest).also {
            it.putExtra(extraName, extra)
            it.action = action
            requireContext().startService(it)
        }
    }
}

