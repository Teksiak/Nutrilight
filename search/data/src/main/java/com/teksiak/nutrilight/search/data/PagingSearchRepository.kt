@file:OptIn(ExperimentalCoroutinesApi::class)

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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PagingSearchRepository @Inject constructor(
    private val apiService: ProductsApiService
) : SearchRepository {
    private val searchQuery = MutableStateFlow("")

    override fun setQuery(query: String) {
        searchQuery.value = query
    }

    override val searchedProducts: Flow<PagingData<Product>>
        get() = searchQuery
            .flatMapLatest { query ->
                Log.d("SEARCH", "query: $query")
                if(query.length >= 3 && query.isNotBlank()) {
                    Pager(
                        config = PagingConfig(
                            pageSize = ProductsApiService.SEARCH_PAGE_SIZE,
                            enablePlaceholders = false
                        ),
                        pagingSourceFactory = {
                            ProductsPagingSource(
                                apiService = apiService,
                                searchQuery = query
                            )
                        }
                    ).flow
                } else flowOf(
                    PagingData.empty()
                )
            }
}