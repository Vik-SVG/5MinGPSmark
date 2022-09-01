package com.priesniakov.a5mingpsmark.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationsReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent?) {
        val pendingResult = goAsync()
        scope.launch {
            try {
                val locationFacade = LocationFacadeImpl(context)
                locationFacade.onSuccessLocationCallback = {
                    Log.d(
                        LocationFacadeImpl.TAG_LOCATION,
                        it?.latitude.toString() + " " +
                                it?.longitude
                    )
                }
                locationFacade.requestLocation()
            } finally {
                pendingResult.finish()
            }
        }
    }
}