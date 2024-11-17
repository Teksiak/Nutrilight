package com.teksiak.nutrilight.product.presentation.product_details

sealed interface ProductDetailsAction {
    data object ToggleNutritionFacts: ProductDetailsAction
    data object ToggleFavourite: ProductDetailsAction
    data object NavigateBack: ProductDetailsAction
}
