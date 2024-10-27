package com.teksiak.nutrilight.product.presentation.history

import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.domain.product.Product

data class HistoryState(
    val productsHistory: List<Product> = emptyList(),
    val showProductImages: Boolean = SettingsRepository.DEFAULT_SHOW_PRODUCT_IMAGES,
    val historySizeSetting: Int = SettingsRepository.DEFAULT_HISTORY_SIZE,
    val isLoading: Boolean = false,
    val productToRemove: String? = null
)