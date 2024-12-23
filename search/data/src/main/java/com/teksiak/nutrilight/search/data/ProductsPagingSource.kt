package com.teksiak.nutrilight.search.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.Result
import com.teksiak.nutrilight.core.network.ProductsApiService
import com.teksiak.nutrilight.core.network.mapper.toProduct
import com.teksiak.nutrilight.core.network.util.safeApiCall
import kotlin.math.ceil

class ProductsPagingSource(
    private val apiService: ProductsApiService,
    private val searchQuery: String,
    private val globalSearch: Boolean = false
): PagingSource<Int, Product>() {

    private var searchResultListener: SearchResultListener? = null

    fun setSearchResultListener(listener: SearchResultListener) {
        searchResultListener = listener
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 1
            val result = safeApiCall {
                if(globalSearch) {
                    apiService.searchProductsGlobally(
                        searchTerms = searchQuery,
                        page = currentPage
                    )
                } else {
                    apiService.searchProducts(
                        searchTerms = searchQuery,
                        page = currentPage
                    )
                }
            }
            if(result is Result.Success) {
                searchResultListener?.onSearchResult(
                    resultCount = result.data.count
                )

                val pageCount = ceil(result.data.count / ProductsApiService.SEARCH_PAGE_SIZE.toFloat()).toInt()
                LoadResult.Page(
                    data = result.data.products.map { it.toProduct() },
                    prevKey = null,
                    nextKey = if(pageCount <= currentPage) null else currentPage + 1
                )
            } else {
                LoadResult.Error(Exception((result as Result.Error).error.name))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
