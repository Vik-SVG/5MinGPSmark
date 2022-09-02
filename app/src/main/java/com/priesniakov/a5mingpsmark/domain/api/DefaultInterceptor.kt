package com.priesniakov.a5mingpsmark.domain.api

import okhttp3.Interceptor
import okhttp3.Response

class DefaultInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Accept", "application/json")
        builder.addHeader("Content-Type", "application/json")
        return chain.proceed(builder.build())
    }
}