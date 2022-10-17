package com.pru.navapp.ui.settings

import com.pru.navapp.base.BaseIntent

sealed class SettingsIntent : BaseIntent() {
    data class GetDetails(var userId: Int) : SettingsIntent()
}