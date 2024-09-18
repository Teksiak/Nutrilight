package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface LocalProductsDataSource {

    fun getProduct(code: String): Flow<Product?>

    suspend fun upsertProduct(product: Product): EmptyResult<DataError.Local>

}