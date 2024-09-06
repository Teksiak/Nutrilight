package com.teksiak.nutrilight.scanner.presentation

sealed interface ScannerAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraPermissionRationale: Boolean
    ): ScannerAction

    class RequestCameraPermission(
        val acceptedCameraPermission: Boolean,
        val showCameraPermissionRationale: Boolean
    ) : ScannerAction

    data object DismissRationaleDialog: ScannerAction
}