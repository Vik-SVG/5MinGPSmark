package com.priesniakov.a5mingpsmark.ui

import android.os.Bundle
import android.view.View
import com.priesniakov.a5mingpsmark.core.BaseFragment
import com.priesniakov.a5mingpsmark.databinding.FragmentHomeBinding
import com.priesniakov.a5mingpsmark.utils.*


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var locationAlarm: LocationAlarm? = null
    private var permissionManager: PermissionManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        getLocationByAlarmBtn.setOnClickListener {
            if (requireContext().checkIfLocationPermissionEnabled()) {
                locationAlarm?.setupLocationAlarm()
            } else {
                permissionManager?.launchPermissionActivity()
            }
        }
        getLocationByServiceBtn.setOnClickListener {

        }
    }

    private fun initInstances() {
        locationAlarm = LocationAlarmImpl(requireContext())
        permissionManager = PermissionManagerImpl(this)
        permissionManager?.onPermissionSuccessCallback = {
            locationAlarm?.setupLocationAlarm()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        permissionManager = null
        locationAlarm = null
    }
}