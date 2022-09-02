package com.priesniakov.a5mingpsmark.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.priesniakov.a5mingpsmark.data.ResourceError
import com.priesniakov.a5mingpsmark.data.ResourceSuccess
import com.priesniakov.a5mingpsmark.domain.LocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationsReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION = "LocationReceiverAction"
    }

    @Inject
    lateinit var locationFacade:LocationFacade

    @Inject
    lateinit var locationUseCase: LocationUseCase

    private val job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent?) {
        val pendingResult = goAsync()
        locationFacade.onSuccessLocationCallback = {
            Log.d(
                LocationFacadeImpl.TAG_LOCATION,
                it?.latitude.toString() + " " +
                        it?.longitude +"Receiver"
            )

            scope.launch {
                locationUseCase.invoke(it).collect() { result ->
                    when (result) {
                        is ResourceSuccess -> pendingResult.finish()
                        is ResourceError -> {
                            Log.e(LocationFacadeImpl.TAG_LOCATION, result.message)
                            pendingResult.finish()
                        }
                        else -> {}
                    }
                }
            }
        }
        locationFacade.requestLocation()
    }
}