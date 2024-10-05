package com.teksiak.nutrilight.core.data

import com.teksiak.nutrilight.core.network.toProduct
import com.teksiak.nutrilight.core.data.util.safeApiCall
import com.teksiak.nutrilight.core.domain.LocalProductsDataSource
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import com.teksiak.nutrilight.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProductsRepositoryImpl @Inject constructor(
    private val apiService: com.teksiak.nutrilight.core.network.ProductsApiService,
    private val localProductsDataSource: LocalProductsDataSource,
    private val applicationScope: CoroutineScope
) : ProductsRepository {

    override suspend fun scanProduct(barcode: String): EmptyResult<DataError.Remote> {
        return when (val result = fetchProduct(barcode)) {
            is Result.Error -> {
                result.asEmptyDataResult()
            }
            is Result.Success -> {
                applicationScope.launch {
                    localProductsDataSource.upsertProduct(result.data).asEmptyDataResult()
                }.join()
                result.asEmptyDataResult()
            }
        }
    }

    override fun getProduct(code: String): Flow<Product?> {
        return localProductsDataSource.getProduct(code)
            .map { product ->
                product ?: try {
                    (fetchProduct(code) as Result.Success).data
                } catch (e: Exception) {
                    null
                }
        }
    }

    override fun getFavouriteProducts(): Flow<List<Product>> = localProductsDataSource.getFavouriteProducts()

    override fun getProductsHistory(): Flow<List<Product>> = localProductsDataSource.getProductsHistory()

    override suspend fun toggleFavourite(code: String): EmptyResult<DataError.Local> = localProductsDataSource.toggleFavourite(code)

    override suspend fun removeFavorite(code: String): EmptyResult<DataError.Local> = localProductsDataSource.removeFavourite(code)

    override suspend fun removeProduct(code: String, ignoreHistory: Boolean): EmptyResult<DataError.Local> = localProductsDataSource.removeProduct(code, ignoreHistory)

    override suspend fun searchProducts(query: String): Result<List<Product>, DataError.Remote> {
        return Result.Success(emptyList())
    }

    private suspend fun fetchProduct(barcode: String): Result<Product, DataError.Remote> {
        return when (val result = safeApiCall {
            apiService.getProduct(barcode)
        }) {
            is Result.Error -> result
            is Result.Success -> {
                if (result.data.status == 0) {
                    Result.Error(DataError.Remote.PRODUCT_NOT_FOUND)
                } else {
                    Result.Success(result.data.toProduct())
                }
            }
        }
    }
}