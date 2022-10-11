package com.pru.navapp.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentSettingsBinding
import com.pru.navapp.listeners.PagingScrollListener
import kotlinx.coroutines.launch


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var adapter: PassengerOtherAdapter

    override fun setup() {
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PassengerOtherAdapter()
        binding.rcView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rcView.adapter = adapter
    }

    override suspend fun observers() {
        repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                viewModel.data.collect {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun listeners() {
        binding.tvView.setOnClickListener {
            viewModel.resetPaging()
        }
        binding.rcView.addOnScrollListener(object : PagingScrollListener() {
            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = viewModel.isLoading

            override fun loadMoreItems() {
                viewModel.fetchData()
            }
        })
    }

}