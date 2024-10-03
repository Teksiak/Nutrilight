package com.teksiak.nutrilight.product.presentation.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
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

    init {
        _state.update { it.copy(isLoading = true) }
        loadProducts()
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.ToggleFavourite -> {
                _state.update { state ->
                    if(!state.favouriteProducts.first { it.code == action.code }.isFavourite) {
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

    private fun loadProducts() {
        productsRepository.getProductsHistory()
            .combine(settingsRepository.getShowProductImages()) { products, showImages ->
                products.map { it.toProductUi(showImages) }
            }
            .onEach { products ->
                _state.update { state ->
                    state.copy(
                        favouriteProducts = products,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}