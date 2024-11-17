package com.teksiak.nutrilight.core.presentation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object HomeRoute : NavigationRoute

    @Serializable
    data object FavouritesRoute : NavigationRoute

    @Serializable
    data object ScannerRoute: NavigationRoute

    @Serializable
    data object HistoryRoute : NavigationRoute

    @Serializable
    data class MoreRoute(
        val information: Information? = null
    ) : NavigationRoute

    @Serializable
    data object SearchRoute : NavigationRoute

    @Serializable
    data class ProductDetailsRoute(
        val productId: String
    ): NavigationRoute
}