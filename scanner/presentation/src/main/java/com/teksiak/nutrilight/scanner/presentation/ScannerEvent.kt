package com.teksiak.nutrilight.scanner.presentation

sealed interface ScannerEvent {
    data class ProductFound(val productId: String): ScannerEvent
}