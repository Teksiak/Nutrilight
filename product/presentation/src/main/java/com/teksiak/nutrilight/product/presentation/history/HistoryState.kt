package com.teksiak.nutrilight.product.presentation.history

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class HistoryState(
    val favouriteProducts: List<ProductUi> = emptyList(),
    val productToRemove: String? = null
)