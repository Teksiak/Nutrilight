package com.teksiak.nutrilight.home

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class HomeState(
    val favouriteProducts: List<ProductUi> = emptyList(),
    val productsHistory: List<ProductUi> = emptyList(),
    val productToRemove: String? = null
)
