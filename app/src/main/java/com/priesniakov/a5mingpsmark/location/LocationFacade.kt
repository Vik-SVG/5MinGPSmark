package com.priesniakov.a5mingpsmark.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.priesniakov.a5mingpsmark.utils.checkIfLocationPermissionEnabled

interface LocationFacade {
    var onSuccessLocationCallback: (Location?) -> Unit
    fun requestLocation()
    fun requestLocationUpdates()
    fun stopLocationUpdates()
}

class LocationFacadeImpl(private val context: Context) : LocationFacade {

    companion object {
        const val TAG_LOCATION = "TagLocation"
        private const val REQUEST_INTERVAL = 1000L * 60 * 5
    }

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = REQUEST_INTERVAL
        fastestInterval = REQUEST_INTERVAL
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    private val updateLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                onSuccessLocationCallback(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates() {
        if (context.checkIfLocationPermissionEnabled()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                updateLocationCallback,
                Looper.myLooper()
            )
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(updateLocationCallback)
    }

    override lateinit var onSuccessLocationCallback: (Location?) -> Unit

    @SuppressLint("MissingPermission")
    override fun requestLocation() {
        if (context.checkIfLocationPermissionEnabled()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                onSuccessLocationCallback(location)
            }
        }
    }
}
