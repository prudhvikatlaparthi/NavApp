package com.pru.navapp.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun Alert(
    showAlert: Boolean,
    title: String?,
    message: String,
    posBtnText: String,
    negBtnText: String?,
    posBtnListener: () -> Unit,
    negBtnListener: () -> Unit
) {
    if (!showAlert) {
        return
    }
    AlertDialog(onDismissRequest = {}, properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ), title = {
        if (title != null) {
            Text(text = title)
        }
    }, text = {
        Text(text = message)
    }, confirmButton = {
        TextButton(onClick = posBtnListener) {
            Text(text = posBtnText)
        }
    }, dismissButton = {
        if (negBtnText != null) {
            TextButton(onClick = negBtnListener) {
                Text(text = negBtnText)
            }
        }
    })
}

data class AlertItem(
    var title: String? = null,
    var message: String,
    var posBtnText: String,
    var negBtnText: String? = null,
    var posBtnListener: () -> Unit = {},
    var negBtnListener: (() -> Unit)? = null,
)