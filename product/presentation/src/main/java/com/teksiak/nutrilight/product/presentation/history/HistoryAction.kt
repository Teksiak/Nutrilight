package com.teksiak.nutrilight.product.presentation.history

sealed interface HistoryAction {
    data class ToggleFavourite(val code: String): HistoryAction
    data object ConfirmRemoveFavourite: HistoryAction
    data object CancelRemoveFavourite : HistoryAction
    data class NavigateToProduct(val code: String): HistoryAction
    data object SearchProduct: HistoryAction
    data object ScanBarcode: HistoryAction
    data object ShowHistorySizeSetting: HistoryAction
    data object NavigateBack: HistoryAction
}