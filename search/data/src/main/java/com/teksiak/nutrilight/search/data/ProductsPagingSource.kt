package com.teksiak.nutrilight.search.data

import android.util.Log
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
    private val searchQuery: String
): PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        Log.d("SEARCH", "load: ${params.key}")
        return try {
            val currentPage = params.key ?: 1
            val result = safeApiCall {
                apiService.searchProducts(
                    searchTerms = searchQuery,
                    page = currentPage
                )
            }
            if(result is Result.Success) {
                Log.d("SEARCH", "success")
                val pageCount = ceil(result.data.count / ProductsApiService.SEARCH_PAGE_SIZE.toFloat()).toInt()
                LoadResult.Page(
                    data = result.data.products.map { it.toProduct() },
                    prevKey = if(currentPage == 1) null else currentPage - 1,
                    nextKey = if(pageCount == currentPage) null else currentPage + 1
                )
            } else {
                Log.d("SEARCH", (result as Result.Error).error.name)
                LoadResult.Error(Exception((result as Result.Error).error.name))
            }
        } catch (e: Exception) {
            Log.d("SEARCH", e.message ?: "error")
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
