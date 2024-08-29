package com.teksiak.nutrilight.core.domain

import com.teksiak.nutrilight.core.domain.product.Product

interface ProductRepository {

    suspend fun getProduct(code: String): Product?

    suspend fun searchProducts(query: String): List<Product>
}