package com.pru.navapp

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.pru.navapp.location.LocationClient
import com.pru.navapp.location.LocationClientSdk
import com.pru.navapp.navigation.AppNavigator
import com.pru.navapp.navigation.AppNavigatorSdk
import com.pru.navapp.remote.ApiService
import com.pru.navapp.repository.ApiRepository
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


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

private var locationClient_: LocationClient? = null

val MainActivity.locationClient: LocationClient
    get() = locationClient_ ?: throw IllegalStateException(
        "Location Client not initialized yet."
    )

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext_ = applicationContext
        appNavigator_ = AppNavigatorSdk()
        apiRepository_ = ApiRepository(apiService = ApiService.getApiService())
        locationClient_ = LocationClientSdk(
            client = LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("font/RobotoCondensed-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}