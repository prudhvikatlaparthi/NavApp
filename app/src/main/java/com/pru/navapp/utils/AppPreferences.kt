package com.pru.navapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pru.navapp.appContext

object AppPreferences : SharedPreferences by appContext.getSharedPreferences(
    "app-preferences", Context.MODE_PRIVATE
) {

    var clickCount: Int
        set(value) = edit {
            putInt("clickCount", value)
        }
        get() = getInt("clickCount", 0)
}