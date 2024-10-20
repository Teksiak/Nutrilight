package com.teksiak.nutrilight.product.presentation.history

import com.teksiak.nutrilight.core.domain.product.Product

data class HistoryState(
    val productsHistory: List<Product> = emptyList(),
    val showProductImages: Boolean = true,
    val historySizeSetting: Int = 0,
    val isLoading: Boolean = false,
    val productToRemove: String? = null
)