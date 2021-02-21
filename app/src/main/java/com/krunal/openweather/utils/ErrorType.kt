package com.krunal.openweather.utils


class ErrorType(val error: ExceptionType, override val message: String? = "") :
    Exception() {

    enum class ExceptionType {
        NETWORK,
        AUTHENTICATION,
        GENERIC
    }
}