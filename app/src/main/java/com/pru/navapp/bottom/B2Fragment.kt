package com.pru.navapp.bottom

import android.os.Bundle
import com.pru.navapp.R
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentB2Binding
import com.pru.navapp.listeners.RefreshListener
import com.pru.navapp.utils.Global.getMainActivity


class B2Fragment : BaseFragment<FragmentB2Binding>(FragmentB2Binding::inflate), RefreshListener {

    override fun onRefresh(any: Any?) {
        binding.tvValue.text = (any as Bundle).getString("VALUE")
    }

    override fun setup() {

    }

    override suspend fun observers() {

    }

    override fun listeners() {
        binding.fabAdd.setOnClickListener {
            getMainActivity().navigate(R.id.action_mainFragment_to_navigation)
        }
    }
}