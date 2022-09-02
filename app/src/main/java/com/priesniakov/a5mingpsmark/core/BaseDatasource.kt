package com.priesniakov.a5mingpsmark.core

import com.priesniakov.a5mingpsmark.data.Resource
import com.priesniakov.a5mingpsmark.data.ResourceError
import com.priesniakov.a5mingpsmark.data.ResourceSuccess
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResults(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()

                if (body != null)
                    return ResourceSuccess(body)
            }
            return ResourceError("Network call failed brcause: ${response.errorBody()?.string()} ")
        } catch (e: Exception) {
            return ResourceError(e.message ?: e.toString())
        }
    }
}