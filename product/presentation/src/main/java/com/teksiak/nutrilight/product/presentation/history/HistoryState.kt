package com.teksiak.nutrilight.product.presentation.history

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class HistoryState(
    val productsHistory: List<ProductUi> = emptyList(),
    val isLoading: Boolean = false,
    val productToRemove: String? = null
)