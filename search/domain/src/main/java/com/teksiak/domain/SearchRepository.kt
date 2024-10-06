package com.teksiak.domain

import androidx.paging.PagingData
import com.teksiak.nutrilight.core.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun setQuery(query: String)

    val searchedProducts: Flow<PagingData<Product>>

}