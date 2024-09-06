package com.teksiak.nutrilight.scanner.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ScannerViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    fun onAction(action: ScannerAction) {
        when (action) {
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