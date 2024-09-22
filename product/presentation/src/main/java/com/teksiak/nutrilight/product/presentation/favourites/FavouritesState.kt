package com.teksiak.nutrilight.product.presentation.favourites

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class FavouritesState(
    val favouriteProducts: List<ProductUi> = emptyList(),
    val productToRemove: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
)