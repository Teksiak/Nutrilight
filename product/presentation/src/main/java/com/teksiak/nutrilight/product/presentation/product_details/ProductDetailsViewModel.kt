package com.teksiak.nutrilight.product.presentation.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val applicationScope: CoroutineScope
): ViewModel()  {

    private val productDetails = savedStateHandle.toRoute<ProductDetailsRoute>()

    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    init {
        loadProduct(productDetails.productId)
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.ToggleNutritionFacts -> {
                _state.update { state ->
                    state.copy(showNutritionFacts = !state.showNutritionFacts)
                }
            }
            is ProductDetailsAction.ToggleFavourite -> {
                viewModelScope.launch {
                    productsRepository.toggleFavourite(productDetails.productId)
                }
            }
            else -> Unit
        }
    }

    private fun loadProduct(productId: String) {
        productsRepository.getProduct(productId)
            .onEach {
                _state.update { state ->
                    state.copy(
                        productUi = it?.toProductUi()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        state.value.productUi?.let {
            if(!it.isFavourite) {
                applicationScope.launch {
                    productsRepository.removeProduct(productDetails.productId)
                }
            }
        }

    }

}