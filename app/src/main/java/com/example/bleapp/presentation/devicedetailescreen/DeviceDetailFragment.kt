package com.example.bleapp.presentation.devicedetailescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bleapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_devices_detail.tv_device_address
import kotlinx.android.synthetic.main.fragment_devices_detail.tv_device_name

@AndroidEntryPoint
class DeviceDetailFragment : Fragment() {
    private val viewModel by viewModels<DevicesDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_devices_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { scanResult ->
            tv_device_name.text = scanResult.scanRecord?.deviceName ?: "N/A"
            tv_device_address.text = scanResult.device.address
        }
    }
}