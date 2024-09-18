package com.teksiak.nutrilight.product.presentation.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.util.Result
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel()  {

    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.LoadProduct -> {
                loadProduct(action.productId)
            }
            is ProductDetailsAction.ToggleNutritionFacts -> {
                _state.update { state ->
                    state.copy(showNutritionFacts = !state.showNutritionFacts)
                }
            }
            else -> Unit
        }
    }

    private fun loadProduct(barcode: String) {
        _state.update { state ->
            state.copy(isLoading = true)
        }
        productsRepository.getProduct(barcode)
            .onEach {
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        productUi = it?.toProductUi()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}