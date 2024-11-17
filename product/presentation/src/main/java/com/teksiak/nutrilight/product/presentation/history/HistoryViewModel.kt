package com.teksiak.nutrilight.product.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    val productsHistory = productsRepository.getProductsHistory()
        .onEach { products ->
            _state.update { state ->
                state.copy(
                    productsHistory = products,
                    isLoading = false
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value.productsHistory
        )

    init {
        _state.update { it.copy(isLoading = true) }

        settingsRepository.showProductImages
            .onEach { showImages ->
                _state.update {
                    it.copy(showProductImages = showImages)
                }
            }
            .launchIn(viewModelScope)

        settingsRepository.historySize
            .onEach { size ->
                _state.update {
                    it.copy(historySizeSetting = size)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.ToggleFavourite -> {
                _state.update { state ->
                    if (state.productsHistory.first { it.code == action.code }.isFavourite != true) {
                        viewModelScope.launch {
                            productsRepository.toggleFavourite(action.code)
                        }
                        state
                    } else {
                        state.copy(productToRemove = action.code)
                    }
                }
            }

            is HistoryAction.ConfirmRemoveFavourite -> {
                _state.value.productToRemove?.let { code ->
                    viewModelScope.launch {
                        productsRepository.removeFavorite(code)
                        _state.update { it.copy(productToRemove = null) }
                    }
                }
            }

            is HistoryAction.CancelRemoveFavourite -> {
                _state.update { it.copy(productToRemove = null) }
            }

            else -> Unit
        }
    }
}