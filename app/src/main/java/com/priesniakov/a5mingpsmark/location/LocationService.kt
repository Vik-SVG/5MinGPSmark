package com.priesniakov.a5mingpsmark.location

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.priesniakov.a5mingpsmark.data.ResourceError
import com.priesniakov.a5mingpsmark.domain.LocationUseCase
import com.priesniakov.a5mingpsmark.utils.ServiceNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationUseCase: LocationUseCase

    @Inject
    lateinit var locationFacade:LocationFacade

    private val job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        moveToForeground()
        locationFacade.onSuccessLocationCallback = {
            scope.launch {
                locationUseCase.invoke(it).collect() { result ->
                    when (result) {
                        is ResourceError -> Log.e(LocationFacadeImpl.TAG_LOCATION, result.message)
                        else -> {}
                    }
                }
            }
        }
        locationFacade.requestLocationUpdates()

        return START_STICKY
    }

    private fun moveToForeground() {
        startForeground(ServiceNotification.ID, ServiceNotification(this).getNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        locationFacade.stopLocationUpdates()
        scope.cancel()
    }
}