package com.teksiak.nutrilight.product.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(isLoading = true) }
        productsRepository.getFavouriteProducts().combine(
            _state.map { it.searchQuery }
        ) { products, searchQuery ->
            products.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
            .onEach { products ->
                _state.update { state ->
                    state.copy(
                        favouriteProducts = products.map { it.toProductUi() },
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
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