package com.teksiak.nutrilight.core.network.util

import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.network.NetworkConstants

fun Country.toBaseUrl(): String {
    return when (this) {
        Country.POLAND -> NetworkConstants.POLAND_BASE_URL
        Country.FRANCE -> NetworkConstants.FRANCE_BASE_URL
        Country.GERMANY -> NetworkConstants.GERMANY_BASE_URL
        Country.ITALY -> NetworkConstants.ITALY_BASE_URL
        Country.SPAIN -> NetworkConstants.SPAIN_BASE_URL
        else -> NetworkConstants.WORLD_BASE_URL
    }
}