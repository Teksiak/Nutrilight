package com.teksiak.nutrilight.product.presentation.favourites

sealed interface FavouritesAction {
    data class RemoveFavourite(val code: String): FavouritesAction
    data object RemoveFavouriteConfirmation: FavouritesAction
    data object RemoveFavouriteCancellation : FavouritesAction
    data class NavigateToProduct(val code: String): FavouritesAction
    data object ToggleSearchbar: FavouritesAction
    data class SearchProducts(val query: String): FavouritesAction
    data object ClearSearch: FavouritesAction
    data object ScanBarcode: FavouritesAction
    data object NavigateBack: FavouritesAction
}