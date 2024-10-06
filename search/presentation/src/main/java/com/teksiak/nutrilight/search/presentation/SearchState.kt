package com.teksiak.nutrilight.search.presentation

import com.teksiak.nutrilight.core.presentation.product.ProductUi

data class SearchState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val hasSearched: Boolean = false,
    val productsHistory: List<ProductUi> = emptyList(),
)
