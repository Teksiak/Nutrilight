package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun scanProduct(barcode: String): EmptyResult<DataError.Remote>

    fun getProduct(code: String): Flow<Product?>

    suspend fun toggleFavourite(code: String): EmptyResult<DataError.Local>

    suspend fun searchProducts(query: String): Result<List<Product>, DataError.Remote>
}