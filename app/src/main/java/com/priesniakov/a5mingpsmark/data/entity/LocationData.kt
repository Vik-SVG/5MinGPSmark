package com.priesniakov.a5mingpsmark.data.entity

import android.location.Location


data class LocationData(
    val latitude: Double?,
    val longitude: Double?,
    val accuracy: Float?,
    val altitude: Double?,
    val bearing: Float?,
    val provider: String?,
    val elapsedRealtimeNanos: Long?,
    val time: Long?,
    val speed: Float?,
    val hasBearing: Boolean?
) {
    constructor(data: Location?) : this(
        data?.latitude,
        data?.longitude,
        data?.accuracy,
        data?.altitude,
        data?.bearing,
        data?.provider,
        data?.elapsedRealtimeNanos,
        data?.time,
        data?.speed,
        data?.hasBearing()
    )
}

data class LocationResponse(val statusCode: Int)