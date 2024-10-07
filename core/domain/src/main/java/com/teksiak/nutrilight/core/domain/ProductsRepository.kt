package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun scanProduct(barcode: String): EmptyResult<DataError.Remote>

    suspend fun addProduct(product: Product): EmptyResult<DataError.Local>

    fun getProduct(code: String): Flow<Product?>

    fun getFavouriteProducts(): Flow<List<Product>>

    fun getProductsHistory(): Flow<List<Product>>

    suspend fun toggleFavourite(code: String): EmptyResult<DataError.Local>

    suspend fun removeFavorite(code: String): EmptyResult<DataError.Local>

    suspend fun removeProduct(code: String, ignoreHistory: Boolean = false): EmptyResult<DataError.Local>
}