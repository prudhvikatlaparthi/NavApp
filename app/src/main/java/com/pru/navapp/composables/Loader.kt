package com.pru.navapp.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pru.navapp.R

@Composable
fun Loader(
    message: String,
    showLoader: Boolean,
    onDismissRequest: () -> Unit
) {
    if (showLoader) {
        Dialog(
            onDismissRequest = onDismissRequest, properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier.size(100.dp),
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White,
            ) {
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = message.takeIf { it.isNotEmpty() }
                            ?: stringResource(id = R.string.loading), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}