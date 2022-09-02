package com.priesniakov.a5mingpsmark.data

sealed class Resource<out T>

data class ResourceSuccess<out T : Any>(val data: T) : Resource<T>()
data class ResourceError<E>(val message: String) : Resource<E>()
object ResourceLoading : Resource<Nothing>()
object ResourceIdle : Resource<Nothing>()