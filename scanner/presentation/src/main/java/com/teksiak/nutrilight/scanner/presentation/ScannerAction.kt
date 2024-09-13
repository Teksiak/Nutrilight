package com.teksiak.nutrilight.scanner.presentation

sealed interface ScannerAction {

    data object ToggleFlash : ScannerAction
    data object NavigateBack : ScannerAction
    data class BarcodeDetected(val barcode: String) : ScannerAction
    data object ScannerError : ScannerAction
    data object DismissError : ScannerAction

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraPermissionRationale: Boolean
    ): ScannerAction

    class RequestCameraPermission(
        val acceptedCameraPermission: Boolean,
        val showCameraPermissionRationale: Boolean
    ) : ScannerAction
}