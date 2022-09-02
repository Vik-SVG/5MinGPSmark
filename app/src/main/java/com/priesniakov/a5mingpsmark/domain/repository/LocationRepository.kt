package com.priesniakov.a5mingpsmark.domain.repository

import com.priesniakov.a5mingpsmark.core.BaseRepository
import com.priesniakov.a5mingpsmark.data.Resource
import com.priesniakov.a5mingpsmark.data.entity.LocationData
import com.priesniakov.a5mingpsmark.data.entity.LocationResponse
import com.priesniakov.a5mingpsmark.domain.datasource.LocationDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    fun sendLocationData(data: LocationData): Flow<Resource<LocationResponse>>
}

@Singleton
class LocationRepositoryImpl @Inject constructor(val datasource: LocationDatasource) : BaseRepository(),
    LocationRepository {

    override fun sendLocationData(data: LocationData) =
        performNetworkCall { datasource.sendLocationData(data) }
}