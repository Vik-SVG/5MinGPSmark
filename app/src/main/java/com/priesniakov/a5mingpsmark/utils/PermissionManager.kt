package com.priesniakov.a5mingpsmark.utils

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

interface PermissionManager {
    fun launchPermissionActivity()
    var onPermissionSuccessCallback: () -> Unit
}

class PermissionManagerImpl(private val fragment: Fragment) : PermissionManager {

    private val PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val BACKGROUND_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    override lateinit var onPermissionSuccessCallback: () -> Unit

    override fun launchPermissionActivity() {
        permissionsResultLauncher.launch(PERMISSIONS)
    }

    private fun launchBackgroundPermissionsActivity() {
        permissionsResultLauncher.launch(BACKGROUND_PERMISSIONS)
    }

    private val permissionsResultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (chekFineAndCoarse(permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (checkBackground(permissions)) {
                    onPermissionSuccessCallback()
                } else {
                    launchBackgroundPermissionsActivity()
                }
            } else {
                onPermissionSuccessCallback()
            }
        }
    }

    private fun chekFineAndCoarse(permissions: Map<String, @JvmSuppressWildcards Boolean>) =
        permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

    private fun checkBackground(permissions: Map<String, @JvmSuppressWildcards Boolean>) =
        permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
}