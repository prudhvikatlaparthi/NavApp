package com.pru.navapp.location

import android.app.PendingIntent
import android.location.Location

sealed class LocationState {
    data class Success(var location: Location) : LocationState()
    object PermissionRequired : LocationState()
    data class GPSRequired(val pendingIntent: PendingIntent) : LocationState()
    data class Failed(var message: String?) : LocationState()
}
