package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalProductsDataSource {

    fun getProduct(code: String): Flow<Product?>

    fun getFavouriteProducts(): Flow<List<Product>>

    fun getProductsHistory(): Flow<List<Product>>

    suspend fun toggleFavourite(code: String): EmptyResult<DataError.Local>

    suspend fun upsertProduct(product: Product): EmptyResult<DataError.Local>

    suspend fun removeProduct(code: String, ignoreHistory: Boolean = true): EmptyResult<DataError.Local>

}