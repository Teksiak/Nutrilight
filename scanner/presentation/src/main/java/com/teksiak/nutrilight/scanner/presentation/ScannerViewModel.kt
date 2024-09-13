package com.teksiak.nutrilight.scanner.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ScannerEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.BarcodeDetected -> {
                _state.update { state ->
                    state.copy(
                        isLoading = true,
                        shouldProcessImage = false
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    when(val result = productsRepository.getProduct(action.barcode)) {
                        is Result.Success -> {
                            withContext(Dispatchers.Main.immediate) {
                                _state.update { state ->
                                    state.copy(
                                        isLoading = false,
                                    )
                                }
                            }
                            launch(Dispatchers.Main) {
                                Log.d("ScannerViewModel", "Product found: ${action.barcode}, ${Thread.currentThread().name}")
                                eventChannel.send(ScannerEvent.ProductFound(action.barcode))
                            }
                        }
                        is Result.Error -> {
                            val productNotFound = result.error == DataError.Remote.PRODUCT_NOT_FOUND
                            withContext(Dispatchers.Main) {
                                _state.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        shouldProcessImage = false,
                                        scannedId = action.barcode,
                                        productNotFound = productNotFound,
                                        scannerError = !productNotFound
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is ScannerAction.ScannerError -> {
                _state.update { state ->
                    state.copy(
                        scannerError = true,
                        shouldProcessImage = false
                    )
                }
            }
            is ScannerAction.DismissError -> {
                _state.update { state ->
                    state.copy(
                        scannedId = null,
                        scannerError = false,
                        productNotFound = false,
                        shouldProcessImage = true
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
                        showCameraPermissionRationale = action.showCameraPermissionRationale,
                        shouldProcessImage = action.acceptedCameraPermission
                    )
                }
            }
            is ScannerAction.RequestCameraPermission -> {
                _state.update { state ->
                    state.copy(
                        requestedCameraPermission = true,
                        acceptedCameraPermission = action.acceptedCameraPermission,
                        showCameraPermissionRationale = action.showCameraPermissionRationale,
                        shouldProcessImage = action.acceptedCameraPermission
                    )
                }
            }
            else -> Unit
        }
    }
}