package dev.gumil.places.presentation

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

internal class LocationObserver(
    context: Context,
    private val onLocationRequested: (Location) -> Unit = {}
) : LifecycleObserver {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    onLocationRequested(it.lastLocation)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startListener() {
        locationClient.requestLocationUpdates(
            LocationRequest().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = DISPLACEMENT
                interval = INTERVAL
            },
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopListener() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val DISPLACEMENT = 100f // in meters
        private const val INTERVAL = 1000L // in milliseconds
    }
}
