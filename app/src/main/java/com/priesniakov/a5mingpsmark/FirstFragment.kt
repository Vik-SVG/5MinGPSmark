package com.priesniakov.a5mingpsmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.priesniakov.a5mingpsmark.core.BaseFragment
import com.priesniakov.a5mingpsmark.databinding.FragmentFirstBinding
import com.priesniakov.a5mingpsmark.utils.LocationFacade
import com.priesniakov.a5mingpsmark.utils.PermissionManager
import com.priesniakov.a5mingpsmark.utils.PermissionManagerImpl
import com.priesniakov.a5mingpsmark.utils.checkIfLocationPermissionEnabled


class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private var locationFacade: LocationFacade? = null
    private var permissionManager: PermissionManager? = null

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonFirst.setOnClickListener {
            if (requireContext().checkIfLocationPermissionEnabled()) {
                locationFacade?.requestLocation()
            } else {
                permissionManager?.launchPermissionActivity()
            }
        }
    }

    private fun initInstances() {
        locationFacade = LocationFacade(requireContext())
        permissionManager = PermissionManagerImpl(this)
        permissionManager?.onPermissionSuccessCallback = {
            locationFacade?.requestLocation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        permissionManager = null
        locationFacade = null
    }
}