package com.priesniakov.a5mingpsmark.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

interface LocationAlarm {
    fun setupLocationAlarm()
}

class LocationAlarmImpl(private val context: Context) : LocationAlarm {
    companion object {
        private const val ALARM_DELAY_IN_SECOND = 10
        private const val ALARM_REQUEST_CODE = 345
        private const val INTERVAL = 1000L * 60 * 1
    }

    private val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val intent = Intent(context, LocationsReceiver::class.java)

    private val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    private val pendingIntent = PendingIntent.getBroadcast(
        context,
        ALARM_REQUEST_CODE,
        intent, intentFlags
    )

    override fun setupLocationAlarm() {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC,
            INTERVAL,
            pendingIntent
        )
    }
}