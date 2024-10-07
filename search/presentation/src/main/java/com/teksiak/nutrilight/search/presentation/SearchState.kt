package com.teksiak.nutrilight.search.presentation

import com.teksiak.nutrilight.core.domain.product.Product

data class SearchState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val hasSearched: Boolean = false,
    val productsHistory: List<Product> = emptyList(),
    val showProductImages: Boolean = true
)
