package com.teksiak.nutrilight.home

import com.teksiak.nutrilight.core.domain.product.Product

data class HomeState(
    val favouriteProducts: List<Product> = emptyList(),
    val productsHistory: List<Product> = emptyList(),
    val showProductImages: Boolean = false,
    val productToRemove: String? = null
)
