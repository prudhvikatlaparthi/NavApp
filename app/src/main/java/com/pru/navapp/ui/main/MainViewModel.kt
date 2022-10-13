package com.pru.navapp.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pru.navapp.R
import com.pru.navapp.appNavigator
import com.pru.navapp.base.BaseViewModel
import com.pru.navapp.listeners.Paging
import com.pru.navapp.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel(), Paging {

    init {
        AppPreferences.clickCount = (0..100).random()


        Log.i("Prudhvi Log", "${AppPreferences.clickCount}: ")
    }

    fun doSomething() {
        Log.i("Prudhvi Log", "doSomething: ")
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            appNavigator.navigate(resId = R.id.action_mainFragment_to_settingsFragment)
        }
    }

    override var pageIndex: Int = 1
    override var isLoading: Boolean = false
    override var isLastPage: Boolean = false

    override fun fetchData() {

    }
}