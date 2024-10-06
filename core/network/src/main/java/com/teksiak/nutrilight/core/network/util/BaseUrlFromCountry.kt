package com.teksiak.nutrilight.core.network.util

import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.network.ProductsApiService

fun Country.toBaseUrl(): String {
    return when (this) {
        Country.POLAND -> ProductsApiService.POLAND_BASE_URL
        Country.FRANCE -> ProductsApiService.FRANCE_BASE_URL
        Country.GERMANY -> ProductsApiService.GERMANY_BASE_URL
        Country.ITALY -> ProductsApiService.ITALY_BASE_URL
        Country.SPAIN -> ProductsApiService.SPAIN_BASE_URL
        else -> ProductsApiService.WORLD_BASE_URL
    }
}