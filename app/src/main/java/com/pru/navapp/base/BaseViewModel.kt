package com.pru.navapp.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : BaseIntent> : ViewModel() {
    abstract fun triggerEvent(intent: T)
}