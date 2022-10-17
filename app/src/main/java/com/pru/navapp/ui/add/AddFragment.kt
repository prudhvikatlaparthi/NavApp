package com.pru.navapp.ui.add

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentAddBinding
import com.pru.navapp.listeners.OnBackPressListener
import com.pru.navapp.utils.Global.setResult


class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate),
    OnBackPressListener {

    override fun backPress() {
        Toast.makeText(requireContext(), "Back", Toast.LENGTH_SHORT).show()
    }

    override fun setup() {

    }

    override suspend fun observers() {

    }

    override fun listeners() {
        binding.btnSave.setOnClickListener {
            setResult(
                AddResultBackValue(
                    value = binding.edtName.text.toString()
                )
            )
            findNavController().popBackStack()
        }
    }
}