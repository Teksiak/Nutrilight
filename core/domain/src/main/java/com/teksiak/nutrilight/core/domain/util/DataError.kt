package com.teksiak.nutrilight.core.domain.util

sealed interface DataError: Error {

    enum class NetworkError: DataError {
        NO_INTERNET,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        UNKNOWN_ERROR
    }

    enum class Local: DataError {
        DISK_FULL,
    }
}