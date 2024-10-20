package com.teksiak.nutrilight.home

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
class HomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val productsHistory = productsRepository.getProductsHistory()
        .onEach { products ->
            _state.update { state ->
                state.copy(
                    productsHistory = products
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value.productsHistory
        )

    val favouriteProducts = productsRepository.getFavouriteProducts()
        .onEach { products ->
            _state.update { state ->
                state.copy(
                    favouriteProducts = products
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value.favouriteProducts
        )

    init {
        settingsRepository.showProductImages
            .onEach { showProductImages ->
                _state.update { state ->
                    state.copy(
                        showProductImages = showProductImages
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.ToggleFavourite -> {
                _state.update { state ->
                    val product = state.productsHistory.firstOrNull { it.code == action.code } ?: state.favouriteProducts.first { it.code == action.code }
                    if (product.isFavourite != true) {
                        viewModelScope.launch {
                            productsRepository.toggleFavourite(action.code)
                        }
                        state
                    } else {
                        state.copy(productToRemove = action.code)
                    }
                }
            }
            is HomeAction.ConfirmRemoveFavourite -> {
                _state.value.productToRemove?.let { code ->
                    viewModelScope.launch {
                        productsRepository.removeFavorite(code)
                        _state.update { it.copy(productToRemove = null) }
                    }
                }
            }
            is HomeAction.CancelRemoveFavourite -> {
                _state.update { it.copy(productToRemove = null) }
            }
            else -> Unit
        }
    }
}