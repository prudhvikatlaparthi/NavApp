package com.pru.navapp.ui.settings

import androidx.lifecycle.viewModelScope
import com.pru.navapp.R
import com.pru.navapp.apiRepository
import com.pru.navapp.appNavigator
import com.pru.navapp.base.BaseViewModel
import com.pru.navapp.ui.composables.AlertItem
import com.pru.navapp.listeners.Paging
import com.pru.navapp.model.response.User
import com.pru.navapp.utils.ApiState
import com.pru.navapp.utils.Constants.kInitialPage
import com.pru.navapp.utils.Global.dismissLoader
import com.pru.navapp.utils.Global.getString
import com.pru.navapp.utils.Global.showAlertDialog
import com.pru.navapp.utils.Global.showLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel(), Paging {

    private val _data = MutableStateFlow(mutableListOf<User>())
    val data: StateFlow<MutableList<User>> = _data
    override var pageIndex = kInitialPage
    override var isLoading = false
    override var isLastPage = false

    init {
        fetchData()
    }

    override fun fetchData() {
        if (pageIndex == kInitialPage) {
            _data.value = mutableListOf()
        }
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getUsers(
                page = pageIndex,
                size = getPageSize()
            ).collect { state ->
                when (state) {
                    is ApiState.Failure -> {
                        isLoading = false
                        dismissLoader()
                        showAlertDialog(
                            alertItem = AlertItem(
                                message = state.errorMessage, posBtnText = "Ok"
                            )
                        )
                    }

                    is ApiState.Loading -> {
                        isLoading = true
                        showLoader(isForInlineProgress = pageIndex != kInitialPage)
                    }

                    is ApiState.Success -> {
                        isLoading = false
                        dismissLoader()
                        if (state.data?.users?.isEmpty() == true) {
                            isLastPage = true
                            if (pageIndex == kInitialPage) {
                                _data.value.clear()
                                showAlertDialog(
                                    alertItem = AlertItem(
                                        message = getString(R.string.msg_no_data),
                                        posBtnText = "Ok"
                                    )
                                )
                                return@collect
                            }
                        }
                        pageIndex++
                        val temp = _data.value.toMutableList()
                        temp.addAll(state.data!!.users!!)
                        _data.value = temp
                    }
                }
            }
        }
    }

    fun details(id: Int) {
        viewModelScope.launch {
            showLoader()
            delay(4000)
            _data.value.filter { it.id == id }.getOrNull(0)?.id?.let {
                appNavigator.navigate(directions =
                SettingsFragmentDirections.actionSettingsFragmentToAboutFragment(
                    it
                )
                )
            }
            dismissLoader()
        }
    }
}