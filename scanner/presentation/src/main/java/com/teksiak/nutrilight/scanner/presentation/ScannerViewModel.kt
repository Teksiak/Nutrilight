package com.teksiak.nutrilight.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.BarcodeDetected -> {
                _state.update { state ->
                    state.copy(isLoading = true)
                }
                viewModelScope.launch {
                    when(val result = productsRepository.getProduct(action.barcode)) {
                        is Result.Success -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    scannedId = action.barcode,
                                    product = result.data
                                )
                            }
                        }
                        is Result.Error -> {
                            val productNotFound = result.error == DataError.Remote.PRODUCT_NOT_FOUND
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    scannedId = action.barcode,
                                    productNotFound = productNotFound,
                                    scannerError = !productNotFound
                                )
                            }
                        }
                    }
                }
            }
            is ScannerAction.ScannerError -> {
                _state.update { state ->
                    state.copy(scannerError = true)
                }
            }
            is ScannerAction.DismissError -> {
                _state.update { state ->
                    state.copy(
                        scannedId = null,
                        scannerError = false,
                        productNotFound = false
                    )
                }
            }
            is ScannerAction.DismissScan -> {
                _state.update { state ->
                    state.copy(
                        scannedId = null,
                        product = null,
                    )
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