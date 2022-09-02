package com.priesniakov.a5mingpsmark.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.priesniakov.a5mingpsmark.R
import com.priesniakov.a5mingpsmark.core.BaseFragment
import com.priesniakov.a5mingpsmark.databinding.FragmentHomeBinding
import com.priesniakov.a5mingpsmark.location.LocationAlarm
import com.priesniakov.a5mingpsmark.location.LocationAlarmImpl
import com.priesniakov.a5mingpsmark.location.LocationService
import com.priesniakov.a5mingpsmark.utils.*
import kotlin.properties.Delegates


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var locationAlarm: LocationAlarm? = null
    private var permissionManager: PermissionManager? = null

    private var isServiceRunning: Boolean by Delegates.observable(false) { _, _, _ ->
        setupServiceBtn()
    }

    private var isAlarmRunning: Boolean by Delegates.observable(false) { _, _, _ ->
        setupAlarmBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        setupButtonObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        permissionManager = null
        locationAlarm = null
    }

    private fun initInstances() {
        locationAlarm = LocationAlarmImpl(requireContext())
        permissionManager = PermissionManagerImpl(this)
    }

    private fun setupButtonObservables() {
        isAlarmRunning = locationAlarm?.isAlarmActive() == true
        isServiceRunning = requireContext().isServiceRunning(
            LocationService::class.java
        )
    }

    private fun setupServiceBtn() = with(binding) {
        getLocationByServiceBtn.text =
            getString(if (isServiceRunning) R.string.stop_location_service else R.string.get_location_by_service)

        getLocationByServiceBtn.setOnClickListener {
            if (isServiceRunning) {
                stopLocationService()
            } else {
                startLocationService()
            }
            isServiceRunning = !isServiceRunning
        }
    }

    private fun setupAlarmBtn() = with(binding) {
        getLocationByAlarmBtn.text =
            getString(if (isAlarmRunning) R.string.stop_location_alarm else R.string.get_location_by_alarm)

        getLocationByAlarmBtn.setOnClickListener {
            if (isAlarmRunning) {
                stopAlarmManager()
            } else {
                startAlarmManager()
            }
            isAlarmRunning = !isAlarmRunning
        }
    }

    private fun startAlarmManager() {
        if (requireContext().checkIfLocationPermissionEnabled()) {
            locationAlarm?.setupLocationAlarm()
        } else {
            permissionManager?.onPermissionSuccessCallback = {
                locationAlarm?.setupLocationAlarm()
            }
            permissionManager?.launchPermissionActivity()
        }
    }

    private fun stopAlarmManager() {
        locationAlarm?.cancelAlarm()
    }

    private fun startLocationService() {
        val startService = {
            requireContext().startService(
                Intent(
                    requireContext(),
                    LocationService::class.java
                )
            )
        }
        if (requireContext().checkIfLocationPermissionEnabled()) {
            startService()
        } else {
            permissionManager?.onPermissionSuccessCallback = {
                startService()
            }
            permissionManager?.launchPermissionActivity()
        }
    }

    private fun stopLocationService() {
        requireContext().stopService(
            Intent(
                requireContext(),
                LocationService::class.java
            )
        )
    }
}