package com.pru.navapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pru.navapp.databinding.PassengerItemBinding
import com.pru.navapp.model.response.PassengerData

class PassengerOtherAdapter :
    ListAdapter<PassengerData, PassengerOtherAdapter.ViewHolder>(differCallback) {
    class ViewHolder(private val binding: PassengerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(passengerData: PassengerData) {
            binding.tvName.text = buildString {
                append(passengerData.id)
                append("\n")
                append(passengerData.name)
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder =
            PassengerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(holder)
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<PassengerData>() {
            override fun areItemsTheSame(oldItem: PassengerData, newItem: PassengerData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PassengerData,
                newItem: PassengerData
            ): Boolean =
                oldItem == newItem

        }
    }

//    private val differ = AsyncListDiffer(this, differCallback)

}