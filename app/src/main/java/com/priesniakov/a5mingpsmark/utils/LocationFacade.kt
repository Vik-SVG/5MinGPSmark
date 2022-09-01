package com.priesniakov.a5mingpsmark.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

interface LocationFacade {
    var onSuccessLocationCallback: (Location?) -> Unit
    fun requestLocation()
}

class LocationFacadeImpl(private val context: Context) : LocationFacade {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        const val TAG_LOCATION = "TagLocation"
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
