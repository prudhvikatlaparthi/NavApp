package com.pru.navapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.pru.navapp.R
import com.pru.navapp.appContext
import com.pru.navapp.utils.Global.getString
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@SuppressLint("MissingPermission")
class LocationClientSdk(
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10000L
    ).build(),
    private val client: FusedLocationProviderClient
) : LocationClient {


    override val locationChannel_: Channel<LocationState> = Channel(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val locationChannel: Flow<LocationState> = locationChannel_.receiveAsFlow()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.lastOrNull()?.let { location ->
                locationChannel_.trySend(LocationState.Success(location = location))
            }

        }
    }

    override fun start() {
        if (!appContext.hasLocationPermission()) {
            locationChannel_.trySend(LocationState.PermissionRequired)
            return
        }

        val playAPI = GoogleApiAvailability.getInstance()
        val resultCode = playAPI.isGooglePlayServicesAvailable(appContext)
        if (resultCode != ConnectionResult.SUCCESS) {
            locationChannel_.trySend(
                LocationState.Failed(
                    getString(
                        R.string.enable_play_services
                    )
                )
            )
            return
        }

        val locationManager =
            appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//            builder.setAlwaysShow(true)
            val result = LocationServices.getSettingsClient(appContext)
                .checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)
                    requestLocation()
                } catch (e: ApiException) {
                    if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            locationChannel_.trySend(e.status.resolution?.let {
                                LocationState.GPSRequired(
                                    it
                                )
                            } ?: LocationState.Failed(e.message))
                        } catch (e: IntentSender.SendIntentException) {
                            locationChannel_.trySend(LocationState.Failed(e.message))
                        } catch (e: ClassCastException) {
                            locationChannel_.trySend(LocationState.Failed(e.message))
                        } catch (e: Exception) {
                            locationChannel_.trySend(LocationState.Failed(e.message))
                        }
                    }
                }
            }
        } else {
            requestLocation()
        }
    }

    override fun requestLocation() {
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun stop() {
        client.removeLocationUpdates(locationCallback)
    }

}