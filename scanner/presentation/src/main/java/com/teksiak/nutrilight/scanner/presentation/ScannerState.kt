package com.teksiak.nutrilight.scanner.presentation

data class ScannerState(
    val scannedId: String? = null,
    val shouldProcessImage: Boolean = false,
    val isFlashOn: Boolean = false,
    val isLoading: Boolean = false,
    val productNotFound: Boolean = false,
    val scannerError: Boolean = false,
    val requestedCameraPermission: Boolean = false,
    val acceptedCameraPermission: Boolean = false,
    val showCameraPermissionRationale: Boolean = false,
)
