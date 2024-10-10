package com.teksiak.nutrilight.product.presentation.favourites

import com.teksiak.nutrilight.core.domain.product.Product

data class FavouritesState(
    val favouriteProducts: List<Product> = emptyList(),
    val showProductImages: Boolean = true,
    val isLoading: Boolean = false,
    val productToRemove: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
)