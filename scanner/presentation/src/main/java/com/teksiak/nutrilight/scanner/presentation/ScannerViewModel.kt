package com.teksiak.nutrilight.scanner.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ScannerViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.SubmitCameraPermissionInfo -> { }
            ScannerAction.DismissRationaleDialog -> { }
        }
    }
}