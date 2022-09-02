package com.priesniakov.a5mingpsmark.location

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface LocationAlarm {
    fun setupLocationAlarm()
    fun cancelAlarm()
    fun isAlarmActive(): Boolean
}

class LocationAlarmImpl @Inject constructor(@ApplicationContext val context: Context) :
    LocationAlarm {
    companion object {
        private const val ALARM_DELAY_IN_SECOND = 10
        private const val ALARM_REQUEST_CODE = 345345
        private const val INTERVAL = 1000L * 60 * 5
    }

    private val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val intent =
        Intent(context, LocationsReceiver::class.java).apply { action = LocationsReceiver.ACTION }

    private val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    private val intentFlagsNoCreate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
    } else {
        PendingIntent.FLAG_NO_CREATE
    }

    private fun getPendingIntent() = PendingIntent.getBroadcast(
        context,
        ALARM_REQUEST_CODE,
        intent, intentFlags
    )

    override fun setupLocationAlarm() {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC,
            INTERVAL,
            getPendingIntent()
        )
    }

    override fun isAlarmActive(): Boolean {
        val pendingIntent: PendingIntent? =
            PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                intent,
                intentFlagsNoCreate
            )
        return pendingIntent != null
    }

    override fun cancelAlarm() {
        alarmManager.cancel(getPendingIntent())
        getPendingIntent().cancel()
    }
}