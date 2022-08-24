package com.example.bleapp.presentation.devicelistscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bleapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_devices_list.btn_scan
import kotlinx.android.synthetic.main.fragment_devices_list.devices_list
import kotlinx.android.synthetic.main.fragment_devices_list.progress_circular
import kotlinx.android.synthetic.main.fragment_devices_list.tv_error
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DevicesListFragment : Fragment() {
    private val viewModel by viewModels<DevicesListViewModel>()
    private val devicesAdapter = DevicesListAdapter(mutableListOf()) {
        viewModel.onDeviceSelected(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_devices_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devices_list.adapter = devicesAdapter
        btn_scan.setOnClickListener { viewModel.startScan() }
        lifecycleScope.launch {
            viewModel.uiState.observe(viewLifecycleOwner) { devicesListState ->
                devicesListState.scanResults?.let {
                    progress_circular.hide()
                    devicesAdapter.updateList(it)
                }
                if (devicesListState.isLoading && devicesListState.scanResults?.isEmpty() == true) {
                    progress_circular.show()
                }
                if (devicesListState.error.isNotBlank()) {
                    tv_error.text = devicesListState.error
                    progress_circular.hide()
                }
            }

        }
    }
}