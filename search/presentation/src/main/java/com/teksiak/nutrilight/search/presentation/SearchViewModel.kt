package com.teksiak.nutrilight.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teksiak.domain.SearchRepository
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository,
    private val applicationScope: CoroutineScope,
) : ViewModel() {
    val searchedProducts = searchRepository.searchedProducts
        .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    init {
        _state.update { state ->
            state.copy(isLoading = true)
        }

        settingsRepository.showProductImages
            .onEach { showImages ->
                _state.update {
                    it.copy(showProductImages = showImages)
                }
            }
            .launchIn(viewModelScope)

        settingsRepository.country
            .onEach { country ->
                _state.update {
                    it.copy(searchedCountry = country)
                }
            }
            .launchIn(viewModelScope)

        searchRepository.searchResultCount
            .onEach { count ->
                _state.update {
                    it.copy(searchResultCount = count)
                }
            }
            .launchIn(viewModelScope)

        loadHistoryProducts()
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
                    it.copy(
                        hasSearched = true,
                        lastShownProductIndex = 0
                    )
                }
                searchRepository.setQuery(state.value.searchQuery)
            }

            is SearchAction.LastShownProductIndexChanged -> {
                _state.update {
                    it.copy(
                        lastShownProductIndex = max(it.lastShownProductIndex, action.index)
                    )
                }
            }

            is SearchAction.ClearSearchQuery -> {
                _state.update {
                    it.copy(searchQuery = "")
                }
            }

            is SearchAction.SearchGlobally -> {
                _state.update {
                    it.copy(
                        searchedGlobally = true,
                        lastShownProductIndex = 0
                    )
                }
                searchRepository.setGlobalSearch(true)
            }

            is SearchAction.NavigateToProduct -> {
                applicationScope.launch {
                    productsRepository.addProduct(action.product)
                }
            }

            is SearchAction.NavigateToNormalSearch -> {
                // TODO: Do some caching with Retrofit to avoid unnecessary API calls
                _state.update {
                    it.copy(
                        searchedGlobally = false,
                        lastShownProductIndex = 0
                    )
                }
                searchRepository.setGlobalSearch(false)
            }

            else -> Unit
        }
    }

    private fun loadHistoryProducts() {
        productsRepository.getProductsHistory()
            .combine(
                _state.map { it.searchQuery }
            ) { products, query ->
                products.filter {
                    it.name.contains(query, ignoreCase = true)
                            || it.code.contains(query, ignoreCase = true)
                            || it.brands?.contains(query, ignoreCase = true) == true
                }
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

    override fun onCleared() {
        super.onCleared()
        searchRepository.setQuery("")
        searchRepository.setGlobalSearch(false)
    }
}