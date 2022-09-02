package com.priesniakov.a5mingpsmark.domain.api

import com.priesniakov.a5mingpsmark.data.entity.LocationData
import com.priesniakov.a5mingpsmark.data.entity.LocationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApi {

    @POST("/api/sendLocation")
    suspend fun sendLocationData(@Body data: LocationData): Response<LocationResponse>
}