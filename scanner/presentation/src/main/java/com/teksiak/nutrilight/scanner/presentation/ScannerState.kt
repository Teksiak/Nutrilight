package com.teksiak.nutrilight.scanner.presentation

import com.teksiak.nutrilight.core.domain.product.Product

data class ScannerState(
    val scannedId: String? = null,
    val product: Product? = null,
    val productNotFound: Boolean = false,
    val isFlashOn: Boolean = false,
    val isLoading: Boolean = false,
    val scannerError: Boolean = false,
    val requestedCameraPermission: Boolean = false,
    val acceptedCameraPermission: Boolean = false,
    val showCameraPermissionRationale: Boolean = false,
)
