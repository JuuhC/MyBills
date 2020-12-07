package com.carrati.domain.models

sealed class Response<out T> {
    class Loading<out T> : Response<T>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure<out T>(val throwable: Throwable) : Response<T>()
}