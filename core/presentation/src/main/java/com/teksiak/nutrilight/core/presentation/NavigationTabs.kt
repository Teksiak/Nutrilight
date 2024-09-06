package com.teksiak.nutrilight.core.presentation

import androidx.annotation.DrawableRes

data class NavTab(
    val title: String,
    @DrawableRes
    val icon: Int,
)

val HomeTab = NavTab(
        title = "Home",
        icon = R.drawable.home,
    )

val HistoryTab = NavTab(
        title = "History",
        icon = R.drawable.history,
    )

val ScannerTab = NavTab(
        title = "Scanner",
        icon = R.drawable.scan_bar,
    )

val FavoritesTab = NavTab(
        title = "Favorites",
        icon = R.drawable.hearts,
    )

val MoreTab = NavTab(
        title = "More",
        icon = R.drawable.more,
    )

fun navigationTabs() = listOf(
    HomeTab,
    HistoryTab,
    ScannerTab,
    FavoritesTab,
    MoreTab,
)