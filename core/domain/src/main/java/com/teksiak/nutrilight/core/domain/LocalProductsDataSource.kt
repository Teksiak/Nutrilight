package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalProductsDataSource {

    fun getProduct(code: String): Flow<Product?>

    suspend fun upsertProduct(product: Product): Result<Unit, DataError.Local>

}