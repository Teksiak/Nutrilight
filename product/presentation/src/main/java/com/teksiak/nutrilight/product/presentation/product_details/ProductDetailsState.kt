package com.teksiak.nutrilight.product.presentation.product_details

import com.teksiak.nutrilight.core.presentation.ui_models.ProductUi

data class ProductDetailsState(
    val productUi: ProductUi? = null,
    val showNutritionFacts: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)