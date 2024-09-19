package com.teksiak.nutrilight.product.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {

    val favouriteProducts = productsRepository
        .getFavouriteProducts()
        .map { products ->
            products.map { it.toProductUi() }
        }

    fun onAction(action: FavouritesAction) {
        when (action) {
            is FavouritesAction.RemoveFavourite -> {
                viewModelScope.launch {
                    productsRepository.toggleFavourite(action.code)
                }
            }

            else -> Unit
        }
    }

}