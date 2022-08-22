package com.example.bleapp.presentation.devicelistscreen

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bleapp.R
import kotlinx.android.synthetic.main.device_item.view.tv_device_address
import kotlinx.android.synthetic.main.device_item.view.tv_device_name
import kotlinx.android.synthetic.main.device_item.view.tv_signal_strength

class DevicesListAdapter(
    private val items: MutableList<ScanResult>,
    private val onItemClickListener: ((device: ScanResult) -> Unit)
) : RecyclerView.Adapter<DevicesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(scanResult: List<ScanResult>) {
        val diffCallback = ScanResultsCallBack(items, scanResult)
        val diffScanResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(scanResult)
        diffScanResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("MissingPermission")
        fun bind(result: ScanResult) {
            itemView.apply {
                tv_device_name.text = result.device.name ?: "N/A"
                tv_device_address.text = result.device.address
                tv_signal_strength.text = "${result.rssi} dBm"
                setOnClickListener { onItemClickListener.invoke(result) }

            }

        }
    }

}
