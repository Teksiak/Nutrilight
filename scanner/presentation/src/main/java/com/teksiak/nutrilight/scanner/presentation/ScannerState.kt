package com.teksiak.nutrilight.scanner.presentation

data class ScannerState(
    val isFlashOn: Boolean = false,
    val isLoading: Boolean = false,
    val hasScannerFailed: Boolean = false,
    val requestedCameraPermission: Boolean = false,
    val acceptedCameraPermission: Boolean = false,
    val showCameraPermissionRationale: Boolean = false,
)
