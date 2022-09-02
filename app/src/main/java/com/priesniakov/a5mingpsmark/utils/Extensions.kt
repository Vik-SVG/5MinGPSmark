package com.priesniakov.a5mingpsmark.utils

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


fun Context.checkIfLocationPermissionEnabled(): Boolean {
    fun checkPermission(permission: String) =
        ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    val mainPermissions = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) && mainPermissions
    } else {
        mainPermissions
    }
}

@Suppress("DEPRECATION")
fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
}