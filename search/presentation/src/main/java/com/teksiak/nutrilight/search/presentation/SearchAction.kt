package com.teksiak.nutrilight.search.presentation

sealed interface SearchAction {
    data class SearchQueryChanged(val query: String): SearchAction
    data object ClearSearchQuery: SearchAction
    data object SearchProducts: SearchAction
    data object ScanBarcode: SearchAction
    data class NavigateToProduct(val productId: String): SearchAction
}