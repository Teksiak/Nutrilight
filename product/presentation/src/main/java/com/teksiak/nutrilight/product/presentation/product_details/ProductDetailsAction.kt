package com.teksiak.nutrilight.product.presentation.product_details

sealed interface ProductDetailsAction {
    data class LoadProduct(val productId: String): ProductDetailsAction
    data object NavigateBack: ProductDetailsAction
}
