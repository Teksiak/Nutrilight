package com.teksiak.nutrilight.product.presentation.product_details

import com.teksiak.nutrilight.core.domain.product.Product

data class ProductDetailsState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)