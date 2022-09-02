package com.priesniakov.a5mingpsmark.location

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.priesniakov.a5mingpsmark.utils.ServiceNotification


class LocationService : Service() {

    private var locationFacade: LocationFacade? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationFacade = LocationFacadeImpl(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        moveToForeground()
        locationFacade?.onSuccessLocationCallback = {
            Log.d(
                LocationFacadeImpl.TAG_LOCATION,
                it?.latitude.toString() + " " +
                        it?.longitude
            )
        }
        locationFacade?.requestLocationUpdates()

        return START_STICKY
    }

    private fun moveToForeground() {
        startForeground(ServiceNotification.ID, ServiceNotification(this).getNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        locationFacade?.stopLocationUpdates()
        locationFacade = null
    }
}