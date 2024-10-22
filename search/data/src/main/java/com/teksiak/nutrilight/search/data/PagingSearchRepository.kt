package com.teksiak.nutrilight.search.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teksiak.domain.SearchRepository
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.network.ProductsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PagingSearchRepository @Inject constructor(
    private val apiService: ProductsApiService
) : SearchRepository {
    private val searchQuery = MutableStateFlow("")
    private val isSearchedGlobally = MutableStateFlow(false)

    private val _searchResultCount = MutableStateFlow(0)
    override val searchResultCount = _searchResultCount.asStateFlow()

    private val searchResultListener = SearchResultListener { resultCount: Int ->
        _searchResultCount.value = resultCount
    }

    override fun setQuery(query: String) {
        searchQuery.value = query
    }

    override fun setGlobalSearch(globalSearch: Boolean) {
        isSearchedGlobally.value = globalSearch
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val searchedProducts: Flow<PagingData<Product>>
        get() = searchQuery
            .combine(isSearchedGlobally) { query, globalSearch ->
                query to globalSearch
            }
            .flatMapLatest { (query, globalSearch) ->
                if(query.isNotBlank()) {
                    Pager(
                        config = PagingConfig(
                            pageSize = ProductsApiService.SEARCH_PAGE_SIZE,
                            enablePlaceholders = false
                        ),
                        pagingSourceFactory = {
                            ProductsPagingSource(
                                apiService = apiService,
                                searchQuery = query,
                                globalSearch = globalSearch
                            ).apply {
                                setSearchResultListener(searchResultListener)
                            }
                        }
                    ).flow
                } else flowOf(
                    PagingData.empty()
                )
            }
}