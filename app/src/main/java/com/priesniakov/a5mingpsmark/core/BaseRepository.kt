package com.priesniakov.a5mingpsmark.core

import com.priesniakov.a5mingpsmark.data.Resource
import com.priesniakov.a5mingpsmark.data.ResourceError
import com.priesniakov.a5mingpsmark.data.ResourceLoading
import com.priesniakov.a5mingpsmark.data.ResourceSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {
    fun <R> performNetworkCall(
        networkCall: suspend () -> Resource<R>,
    ): Flow<Resource<R>> =
        flow {
            emit(ResourceLoading)
            val networkResponse = networkCall.invoke()

            if (networkResponse is ResourceSuccess) {
                emit(networkResponse)

            } else if (networkResponse is ResourceError)
                emit(ResourceError(networkResponse.message))
        }.flowOn(Dispatchers.IO)
}