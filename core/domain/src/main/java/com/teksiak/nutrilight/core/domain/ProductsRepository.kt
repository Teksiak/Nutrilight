package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result

interface ProductsRepository {

    suspend fun getProduct(barcode: String): Result<Product, DataError.Remote>

    suspend fun searchProducts(query: String): Result<List<Product>, DataError.Remote>
}