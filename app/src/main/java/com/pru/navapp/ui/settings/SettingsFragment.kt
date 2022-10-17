package com.pru.navapp.ui.settings

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentSettingsBinding
import com.pru.navapp.listeners.PagingScrollListener
import com.pru.navapp.model.response.User
import com.pru.navapp.utils.Global.getLocation
import com.pru.navapp.utils.Global.showLoader
import com.pru.navapp.utils.Global.showToast
import kotlinx.coroutines.launch


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel by viewModels<SettingsViewModel>()
    private val adapter: UserAdapter by lazy {
        UserAdapter(differCallback) {
            viewModel.triggerEvent(SettingsIntent.GetDetails(userId = it.id!!))
        }
    }

    override fun setup() {
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.addItemDecoration(
            DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL
            )
        )
        binding.rcView.adapter = adapter
        if (viewModel.data.value.size == 0) {
            getInitialData()
        }
    }

    override suspend fun observers() {
        repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                viewModel.data.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun listeners() {
        binding.tvView.setOnClickListener {
            getInitialData()
        }
        binding.rcView.addOnScrollListener(object : PagingScrollListener() {
            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = viewModel.isLoading

            override fun loadMoreItems() {
                viewModel.fetchData()
            }
        })
    }

    private fun getInitialData() {
        showLoader(message = "Fetching Location")
        getLocation {
            showToast(it.latitude.toString())
            viewModel.resetPaging()
        }
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: User, newItem: User
            ): Boolean = oldItem == newItem

        }
    }

}