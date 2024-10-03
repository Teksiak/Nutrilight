@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.product.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesState())
    val state = _state.asStateFlow()

    val favouriteProducts = _state.map { it.searchQuery }
        .flatMapLatest { searchQuery ->
            productsRepository.getFavouriteProducts()
                .combine(settingsRepository.getShowProductImages()) { products, showImages ->
                    products
                        .filter {
                            it.name.contains(searchQuery, ignoreCase = true)
                                    || it.brands?.contains(searchQuery, ignoreCase = true) == true
                        }
                        .map { it.toProductUi(showImages) }
                }
        }
        .onEach { products ->
            _state.update { state ->
                state.copy(
                    favouriteProducts = products,
                    isLoading = false
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value.favouriteProducts
        )

    init {
        _state.update { it.copy(isLoading = true) }
    }

    fun onAction(action: FavouritesAction) {
        when (action) {
            is FavouritesAction.RemoveFavourite -> {
                _state.update { it.copy(productToRemove = action.code) }
            }

            is FavouritesAction.ConfirmRemoveFavourite -> {
                _state.value.productToRemove?.let { code ->
                    viewModelScope.launch {
                        productsRepository.removeFavorite(code)
                        _state.update { it.copy(productToRemove = null) }
                    }
                }
            }

            is FavouritesAction.CancelRemoveFavourite -> {
                _state.update { it.copy(productToRemove = null) }
            }

            is FavouritesAction.ToggleSearchbar -> {
                _state.update { it.copy(isSearchActive = !it.isSearchActive) }
            }

            is FavouritesAction.SearchProducts -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            is FavouritesAction.ClearSearch -> {
                _state.update { it.copy(searchQuery = "") }
            }

            else -> Unit
        }
    }
}