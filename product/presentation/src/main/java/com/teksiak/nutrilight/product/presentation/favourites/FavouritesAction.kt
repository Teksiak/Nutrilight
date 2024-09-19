package com.teksiak.nutrilight.product.presentation.favourites

sealed interface FavouritesAction {
    data class RemoveFavourite(val code: String): FavouritesAction
    data class NavigateToProduct(val code: String): FavouritesAction
    data object NavigateBack: FavouritesAction
}