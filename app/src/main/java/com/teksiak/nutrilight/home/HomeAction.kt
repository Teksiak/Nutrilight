package com.teksiak.nutrilight.home

sealed interface HomeAction {
    data object ScanBarcode: HomeAction
    data object NavigateToProductsHistory: HomeAction
    data object NavigateToFavouriteProducts: HomeAction
    data class NavigateToProduct(val code: String): HomeAction
    data class ToggleFavourite(val code: String): HomeAction
    data object ConfirmRemoveFavourite: HomeAction
    data object CancelRemoveFavourite: HomeAction

}