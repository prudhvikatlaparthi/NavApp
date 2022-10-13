package com.pru.navapp.ui.settings

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pru.navapp.base.BaseAdapter
import com.pru.navapp.databinding.PassengerItemBinding
import com.pru.navapp.model.response.PassengerData
import com.pru.navapp.utils.Global

class PassengerBAdapter(
    diffCallback: DiffUtil.ItemCallback<PassengerData>
) : BaseAdapter<PassengerData, PassengerItemBinding>(
    PassengerItemBinding::inflate,
    diffCallback
) {

    override fun bindData(binding: PassengerItemBinding, item: PassengerData) {
        binding.tvName.text = buildString {
            append(item.id)
            append("\n")
            append(item.name)
        }
    }

    override fun clickListener(binding: PassengerItemBinding, holderCallback: () -> Int) {

        binding.tvName.setOnClickListener {
            val pos = holderCallback.position
            if (pos != RecyclerView.NO_POSITION) {
                Global.showToast(getItem(pos).name)
            }
        }
    }
}