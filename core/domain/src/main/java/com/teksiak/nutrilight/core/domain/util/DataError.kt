package com.teksiak.nutrilight.core.domain.util

sealed interface DataError: Error {

    enum class Remote: DataError {
        NO_INTERNET,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        SERIALIZATION_ERROR,
        SERVER_ERROR,
        PRODUCT_NOT_FOUND,
        UNKNOWN_ERROR,
    }

    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN_ERROR,
    }
}