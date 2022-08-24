package com.example.bleapp.presentation.devicedetailescreen

import android.bluetooth.BluetoothGattCharacteristic
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bleapp.R
import com.example.bleapp.model.CharacteristicFactory
import com.example.bleapp.model.CharacteristicReadingInterface
import kotlinx.android.synthetic.main.characteristic_image_item.view.img_char_value_img
import kotlinx.android.synthetic.main.characteristic_image_item.view.tv_char_uuid_img
import kotlinx.android.synthetic.main.characteristic_text_item.view.tv_char_uuid_text
import kotlinx.android.synthetic.main.characteristic_text_item.view.tv_char_value_text

const val TEXT_VIEW_TYPE = 0
const val IMAGE_VIEW_TYPE = 1

class CharacteristicAdapter(
    private val items: MutableList<BluetoothGattCharacteristic>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val characteristicFactory = CharacteristicFactory()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == IMAGE_VIEW_TYPE) {
            ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.characteristic_image_item, parent, false)
            )
        } else {
            TexViewtHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.characteristic_text_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (CharacteristicFactory().isBitmap(items[position])) {
            IMAGE_VIEW_TYPE
        } else {
            TEXT_VIEW_TYPE
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (characteristicFactory.isBitmap(item)) {
            (holder as ImageViewHolder).bind(characteristicFactory.createCharacteristic(item))
        } else {
            (holder as TexViewtHolder).bind(characteristicFactory.createCharacteristic(item))
        }

    }

    fun updateList(characteristics: List<BluetoothGattCharacteristic>) {
        items.clear()
        items.addAll(characteristics)
        notifyDataSetChanged()
    }

    inner class TexViewtHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(characteristic: CharacteristicReadingInterface<String>) {
            itemView.apply {
                tv_char_uuid_text.text =
                    (characteristic as BluetoothGattCharacteristic).uuid.toString()
                tv_char_value_text.text = characteristic.getParsedValue()
            }

        }
    }

    inner class ImageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(characteristic: CharacteristicReadingInterface<Bitmap>) {
            itemView.apply {
                tv_char_uuid_img.text =
                    (characteristic as BluetoothGattCharacteristic).uuid.toString()
                img_char_value_img.setImageBitmap(characteristic.getParsedValue())

            }

        }
    }

}
