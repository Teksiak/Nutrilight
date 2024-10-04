package com.teksiak.nutrilight.core.presentation

enum class BottomNavigationTab {
    Home,
    History,
    Scanner,
    Favourites,
    More
}

fun BottomNavigationTab.getIcon(): Int {
    return when (this) {
        BottomNavigationTab.Home -> R.drawable.home
        BottomNavigationTab.History -> R.drawable.history
        BottomNavigationTab.Scanner -> R.drawable.scan_bar
        BottomNavigationTab.Favourites -> R.drawable.hearts
        BottomNavigationTab.More -> R.drawable.more
    }
}

fun BottomNavigationTab.toRoute(): Any {
    return when (this) {
        BottomNavigationTab.Home -> NavigationRoute.HomeRoute
        BottomNavigationTab.History -> NavigationRoute.HistoryRoute
        BottomNavigationTab.Scanner -> NavigationRoute.ScannerRoute
        BottomNavigationTab.Favourites -> NavigationRoute.FavouritesRoute
        BottomNavigationTab.More -> NavigationRoute.MoreRoute
    }
}