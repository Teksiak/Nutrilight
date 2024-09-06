package com.teksiak.nutrilight.home

sealed interface HomeAction {
    data object ScanBarcode: HomeAction
}