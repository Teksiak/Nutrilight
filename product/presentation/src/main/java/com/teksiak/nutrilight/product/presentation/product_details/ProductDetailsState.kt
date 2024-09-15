package com.teksiak.nutrilight.product.presentation.product_details

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class ProductDetailsState(
    val productUi: ProductUi? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)