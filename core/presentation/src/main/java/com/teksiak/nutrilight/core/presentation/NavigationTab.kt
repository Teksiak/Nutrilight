package com.teksiak.nutrilight.core.presentation

enum class NavigationTab {
    Home,
    History,
    Scanner,
    Favourites,
    More
}

fun NavigationTab.getIcon(): Int {
    return when (this) {
        NavigationTab.Home -> R.drawable.home
        NavigationTab.History -> R.drawable.history
        NavigationTab.Scanner -> R.drawable.scan_bar
        NavigationTab.Favourites -> R.drawable.hearts
        NavigationTab.More -> R.drawable.more
    }
}