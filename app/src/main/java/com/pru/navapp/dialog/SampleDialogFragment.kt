package com.pru.navapp.dialog

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.pru.navapp.R


class SampleDialogFragment : DialogFragment(R.layout.fragment_sample_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        view.findViewById<Button>(R.id.next).setOnClickListener {
            findNavController().navigate(
                SampleDialogFragmentDirections.actionSampleDialogFragmentToAddFragment(
                    10
                )
            )
        }
    }
}