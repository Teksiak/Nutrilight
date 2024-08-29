package com.teksiak.nutrilight.core.domain.remote

import com.teksiak.nutrilight.core.domain.product.Product

interface RemoteProductDataSource {

    suspend fun getProduct(code: String): Product?

    suspend fun searchProducts(query: String): List<Product>
}