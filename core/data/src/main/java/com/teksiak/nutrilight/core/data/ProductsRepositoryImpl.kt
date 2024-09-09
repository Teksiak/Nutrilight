package com.teksiak.nutrilight.core.data

import android.util.Log
import com.teksiak.nutrilight.core.data.mapper.toProduct
import com.teksiak.nutrilight.core.data.util.safeApiCall
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import com.teksiak.nutrilight.core.domain.util.map
import javax.inject.Inject


class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ProductsApiService
): ProductsRepository {

    override suspend fun getProduct(barcode: String): Result<Product, DataError.Remote> {
        val result = safeApiCall {
            apiService.getProduct(barcode)
        }

        if(result is Result.Success && result.data.status == 0) {
            return Result.Error(DataError.Remote.PRODUCT_NOT_FOUND)
        }

        return result.map { it.toProduct() }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>, DataError.Remote> {
        return Result.Success(emptyList())
    }
}