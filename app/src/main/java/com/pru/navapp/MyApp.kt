package com.pru.navapp

import android.app.Application
import android.content.Context
import com.pru.navapp.navigation.AppNavigator
import com.pru.navapp.navigation.AppNavigatorSdk
import com.pru.navapp.remote.ApiService
import com.pru.navapp.repository.ApiRepository

private var appContext_: Context? = null
val appContext: Context
    get() = appContext_
        ?: throw IllegalStateException(
            "Application context not initialized yet."
        )

private var appNavigator_: AppNavigator? = null
val appNavigator: AppNavigator
    get() = appNavigator_ ?: throw IllegalStateException(
        "App Navigator not initialized yet."
    )

private var apiRepository_: ApiRepository? = null
val apiRepository: ApiRepository
    get() = apiRepository_ ?: throw IllegalStateException(
        "Api Repository not initialized yet."
    )

private val repository: ApiRepository = ApiRepository(apiService = ApiService.getApiService())

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext_ = applicationContext
        appNavigator_ = AppNavigatorSdk()
        apiRepository_ = ApiRepository(apiService = ApiService.getApiService())
    }
}