package com.krunal.openweather.utils


sealed class Response<T> {
    class Success<T>(val data: T) : Response<T>()
    class Error<T>(
        val message: String? = "",
        val error: Throwable? = null,
        val data: T? = null
    ) :
        Response<T>()
}
