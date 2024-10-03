package com.teksiak.nutrilight.home

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class HomeState(
    val isLoading: Boolean = false,
    val favouriteProducts: List<ProductUi>,
    val historyProducts: List<ProductUi>
)
