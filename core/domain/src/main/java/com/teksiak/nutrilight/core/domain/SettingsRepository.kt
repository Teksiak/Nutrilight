package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val showProductImages: Flow<Boolean>
    val country: Flow<Country>
    val historySize: Flow<Int>
    suspend fun toggleShowProductImages(): EmptyResult<DataError.Local>
    suspend fun setCountry(countryCode: String): EmptyResult<DataError.Local>
    suspend fun setHistorySize(size: Int): EmptyResult<DataError.Local>

    companion object {
        const val DATASTORE_NAME = "settings"
        const val SHOW_PRODUCT_IMAGES_NAME = "show_product_images"
        const val COUNTRY_NAME = "country"
        const val HISTORY_SIZE = "history_size"

        const val DEFAULT_HISTORY_SIZE = 10
        const val DEFAULT_SHOW_PRODUCT_IMAGES = true
        val DEFAULT_COUNTRY = Country.UNITED_KINGDOM

        val HISTORY_SIZES = (5..25).toList()
    }
}