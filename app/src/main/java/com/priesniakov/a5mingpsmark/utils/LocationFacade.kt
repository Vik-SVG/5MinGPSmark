package com.priesniakov.a5mingpsmark.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class LocationFacade(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        const val TAG_LOCATION = "TagLocation"
    }

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        if (context.checkIfLocationPermissionEnabled()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d(
                    TAG_LOCATION,
                    location?.latitude.toString() + " " +
                            location?.longitude
                )
            }
        }
    }
}
