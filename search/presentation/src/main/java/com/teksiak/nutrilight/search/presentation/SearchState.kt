package com.teksiak.nutrilight.search.presentation

import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.domain.product.Product

data class SearchState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val hasSearched: Boolean = false,
    val searchedCountry: Country? = null,
    val searchedGlobally: Boolean = false,
    val searchResultCount: Int = 0,
    val lastShownProductIndex: Int = -1,
    val productsHistory: List<Product> = emptyList(),
    val productToRemove: String? = null,
    val showProductImages: Boolean = true
)
