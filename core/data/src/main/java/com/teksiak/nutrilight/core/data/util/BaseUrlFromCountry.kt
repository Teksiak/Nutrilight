package com.teksiak.nutrilight.core.data.util

import com.teksiak.nutrilight.core.data.Constants
import com.teksiak.nutrilight.core.domain.Country

fun Country.toBaseUrl(): String {
    return when (this) {
        Country.POLAND -> Constants.POLAND_BASE_URL
        Country.FRANCE -> Constants.FRANCE_BASE_URL
        Country.GERMANY -> Constants.GERMANY_BASE_URL
        Country.ITALY -> Constants.ITALY_BASE_URL
        Country.SPAIN -> Constants.SPAIN_BASE_URL
        else -> Constants.WORLD_BASE_URL
    }
}