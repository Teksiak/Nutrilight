package com.teksiak.nutrilight.search.presentation

import com.teksiak.nutrilight.core.domain.product.Product

sealed interface SearchAction {
    data class SearchQueryChanged(val query: String): SearchAction
    data object ClearSearchQuery: SearchAction
    data object SearchProducts: SearchAction
    data class LastShownProductIndexChanged(val index: Int): SearchAction
    data object SearchGlobally: SearchAction
    data object ScanBarcode: SearchAction
    data class NavigateToProduct(val product: Product): SearchAction
    data object NavigateToNormalSearch: SearchAction
    data object NavigateBack: SearchAction
}