package com.teksiak.nutrilight.scanner.presentation

data class ScannerState(
    val requestedCameraPermission: Boolean = false,
    val acceptedCameraPermission: Boolean = false,
    val showCameraPermissionRationale: Boolean = false,
)
