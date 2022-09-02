package com.priesniakov.a5mingpsmark.domain.datasource

import com.priesniakov.a5mingpsmark.core.BaseDataSource
import com.priesniakov.a5mingpsmark.data.Resource
import com.priesniakov.a5mingpsmark.data.entity.LocationData
import com.priesniakov.a5mingpsmark.data.entity.LocationResponse
import com.priesniakov.a5mingpsmark.domain.api.LocationApi
import javax.inject.Inject
import javax.inject.Singleton

interface LocationDatasource {
    suspend fun sendLocationData(data: LocationData): Resource<LocationResponse>
}

@Singleton
class LocationDatasourceImpl @Inject constructor(val api: LocationApi) : BaseDataSource(),
    LocationDatasource {

    override suspend fun sendLocationData(data: LocationData) =
        getResults { api.sendLocationData(data) }
}