package com.pru.navapp.utils

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pru.navapp.ui.composables.AlertItem

object ComposableUtils {
    init {
        Log.i("Prudhvi Log", ": ComposableUtils")
    }

    var loaderMessage: String = ""
    var loaderState by mutableStateOf(false)
    var inlineProgressState by mutableStateOf(false)
    var alertItem = AlertItem(
        title = null,
        message = "",
        posBtnText = "",
        negBtnText = null,
        posBtnListener = {},
        negBtnListener = {})
    var alertDialogState by mutableStateOf(false)
}