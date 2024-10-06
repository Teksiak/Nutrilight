package com.teksiak.nutrilight.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.teksiak.domain.SearchRepository
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    val searchedProducts = searchRepository
        .searchedProducts
        .combine(settingsRepository.showProductImages) { pagingData, showImages ->
            pagingData.map { product ->
                product.toProductUi(showImages)
            }
        }
        .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    init {
        _state.update { state ->
            state.copy(isLoading = true)
        }

        productsRepository.getProductsHistory()
            .combine(_state.map { it.searchQuery }) { products, query ->
                products.filter { it.name.contains(query, ignoreCase = true)
                        || it.code.contains(query, ignoreCase = true)
                        || it.brands?.contains(query, ignoreCase = true) == true
                }
            }
            .combine(settingsRepository.showProductImages) { products, showImages ->
                products.map { it.toProductUi(showImages) }
            }
            .onEach { products ->
                _state.update { state ->
                    state.copy(
                        productsHistory = products,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.SearchQueryChanged -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            is SearchAction.SearchProducts -> {
                _state.update {
                    it.copy(hasSearched = true)
                }
                searchRepository.setQuery(state.value.searchQuery)
            }
            is SearchAction.ClearSearchQuery -> {
                _state.update {
                    it.copy(searchQuery = "")
                }
            }

            else -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchRepository.setQuery("")
    }
}