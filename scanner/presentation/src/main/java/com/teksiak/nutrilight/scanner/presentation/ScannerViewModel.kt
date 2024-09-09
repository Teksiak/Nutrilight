package com.teksiak.nutrilight.scanner.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ScannerViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.BarcodeDetected -> {
                _state.update { state ->
                    state.copy(isLoading = true)
                }
            }
            is ScannerAction.ScannerError -> {
                _state.update { state ->
                    state.copy(hasScannerFailed = true)
                }
            }
            is ScannerAction.DismissScannerError -> {
                _state.update { state ->
                    state.copy(hasScannerFailed = false)
                }
            }
            is ScannerAction.ToggleFlash -> {
                _state.update { state ->
                    state.copy(isFlashOn = !state.isFlashOn)
                }
            }
            is ScannerAction.SubmitCameraPermissionInfo -> {
                _state.update { state ->
                    state.copy(
                        acceptedCameraPermission = action.acceptedCameraPermission,
                        showCameraPermissionRationale = action.showCameraPermissionRationale
                    )
                }
            }
            is ScannerAction.RequestCameraPermission -> {
                _state.update { state ->
                    state.copy(
                        requestedCameraPermission = true,
                        acceptedCameraPermission = action.acceptedCameraPermission,
                        showCameraPermissionRationale = action.showCameraPermissionRationale
                    )
                }
            }
            else -> Unit
        }
    }
}