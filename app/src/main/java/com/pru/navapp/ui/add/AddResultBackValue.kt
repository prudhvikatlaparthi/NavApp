package com.pru.navapp.ui.add

import com.pru.navapp.base.ResultBack
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddResultBackValue(
    var value: String,
) : ResultBack()
