package com.pru.navapp.fragments

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.pru.navapp.base.BaseFragment
import com.pru.navapp.databinding.FragmentAddBinding
import com.pru.navapp.listeners.OnBackPressListener


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
            setFragmentResult(
                "KEY_RESULT",
                bundleOf("VALUE" to binding.edtName.text.toString())
            )
            findNavController().popBackStack()
        }
    }
}