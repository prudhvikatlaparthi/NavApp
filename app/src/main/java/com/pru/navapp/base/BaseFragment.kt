package com.pru.navapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater) -> VB) :
    Fragment() {
    private var showHomeAsUp = false

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding as VB

    fun setBack(value: Boolean) {
        showHomeAsUp = value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        viewLifecycleOwner.lifecycleScope.launch {
            observers()
        }
        listeners()
    }

    abstract fun setup()

    abstract suspend fun observers()

    abstract fun listeners()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}