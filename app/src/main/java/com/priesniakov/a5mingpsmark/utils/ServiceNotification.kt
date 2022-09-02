package com.priesniakov.a5mingpsmark.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.priesniakov.a5mingpsmark.MainActivity
import com.priesniakov.a5mingpsmark.R

class ServiceNotification(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "channel_location"
        private const val CHANNEL_NAME = "channel_location"
        private const val REQUEST_CODE = 754
        const val ID = 153
    }

    fun getNotification(): Notification {

        val builder: NotificationCompat.Builder
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(getNotificationChannel())
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
            builder.setChannelId(CHANNEL_ID)
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
        } else {
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
        }

        builder.apply {
            setContentTitle(context.getString(R.string.location_service))
            setContentText(context.getString(R.string.gathering_location))
            setSound(getNotificationSound())
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_location_notification)
            setContentIntent(getPendingIntent())
        }
        return builder.build()
    }

    private fun getNotificationSound(): Uri = RingtoneManager.getActualDefaultRingtoneUri(
        context,
        RingtoneManager.TYPE_NOTIFICATION
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(): NotificationChannel {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        return channel
    }

    private fun getPendingIntent(): PendingIntent? {
        val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            intentFlags
        )
    }
}