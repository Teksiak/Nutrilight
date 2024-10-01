package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getShowProductImages(): Flow<Boolean>
    fun getCountryCode(): Flow<Country>
    suspend fun toggleShowProductImages(): EmptyResult<DataError.Local>
    suspend fun setCountryCode(countryCode: String): EmptyResult<DataError.Local>

    companion object {
        const val DATASTORE_NAME = "settings"
        const val SHOW_PRODUCT_IMAGES_NAME = "show_product_images"
        const val COUNTRY_NAME = "country"
    }
}