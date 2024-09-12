package com.teksiak.nutrilight.product.presentation.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            else -> Unit
        }
    }

    private fun loadProduct(barcode: String) {
        _state.update { state ->
            state.copy(isLoading = true)
        }
        viewModelScope.launch {
            when(val result = productsRepository.getProduct(barcode)) {
                is Result.Success -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            product = result.data
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Something went wrong" // TODO: parse error message
                        )
                    }
                }
            }
        }
    }

}