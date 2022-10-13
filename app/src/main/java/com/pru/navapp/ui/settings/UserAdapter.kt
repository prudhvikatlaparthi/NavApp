package com.pru.navapp.ui.settings

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pru.navapp.base.BaseAdapter
import com.pru.navapp.databinding.ItemUserLayoutBinding
import com.pru.navapp.model.response.User

class UserAdapter(
    diffCallback: DiffUtil.ItemCallback<User>,
    val listener: (User) -> Unit
) : BaseAdapter<User, ItemUserLayoutBinding>(
    ItemUserLayoutBinding::inflate,
    diffCallback
) {

    override fun bindData(binding: ItemUserLayoutBinding, item: User) {
        binding.tvName.text = buildString {
            append(item.id)
            append("\n")
            append(item.firstName.plus(" ").plus(item.lastName))
            append("\n")
            append(item.email)
        }
    }

    override fun clickListener(binding: ItemUserLayoutBinding, holderCallback: () -> Int) {
        binding.tvName.setOnClickListener {
            val pos = holderCallback.position
            if (pos != RecyclerView.NO_POSITION) {
                listener.invoke(getItem(pos))
            }
        }
    }


}