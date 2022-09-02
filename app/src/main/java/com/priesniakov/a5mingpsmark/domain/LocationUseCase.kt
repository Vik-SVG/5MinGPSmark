package com.priesniakov.a5mingpsmark.domain

import android.location.Location
import com.priesniakov.a5mingpsmark.data.Resource
import com.priesniakov.a5mingpsmark.data.entity.LocationData
import com.priesniakov.a5mingpsmark.data.entity.LocationResponse
import com.priesniakov.a5mingpsmark.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocationUseCase {
    fun invoke(data: LocationData): Flow<Resource<LocationResponse>>
    fun invoke(data: Location?): Flow<Resource<LocationResponse>>
}

class LocationUseCaseImpl @Inject constructor(val repository: LocationRepository) :
    LocationUseCase {

    override fun invoke(data: LocationData) =
        repository.sendLocationData(data)

    override fun invoke(data: Location?) =
        repository.sendLocationData(LocationData(data))
}