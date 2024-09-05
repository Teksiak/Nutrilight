package com.teksiak.nutrilight.scanner.presentation

data class ScannerState(
    val acceptedCameraPermission: Boolean = false,
    val showCameraPermissionRationale: Boolean = false,
)
