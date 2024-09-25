package com.teksiak.nutrilight.core.database

import android.database.sqlite.SQLiteFullException
import com.teksiak.nutrilight.core.database.dao.ProductsDao
import com.teksiak.nutrilight.core.database.mapper.toProduct
import com.teksiak.nutrilight.core.database.mapper.toProductEntity
import com.teksiak.nutrilight.core.domain.LocalProductsDataSource
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalProductsDataSource @Inject constructor(
    private val productsDao: ProductsDao,
): LocalProductsDataSource {
    override fun getProduct(code: String): Flow<Product?> {
        return productsDao.getProduct(code).map {
            it?.toProduct()
        }
    }

    override fun getFavouriteProducts(): Flow<List<Product>> {
        return productsDao.getProducts()
            .map { productsList ->
                productsList
                    .filter { it.isFavourite }
                    .map { productEntity ->
                        productEntity.toProduct()
                    }
            }
    }

    override suspend fun toggleFavourite(code: String): EmptyResult<DataError.Local> {
        return try {
            productsDao.toggleFavourite(code)
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertProduct(product: Product): EmptyResult<DataError.Local> {
        return try {
            val existingProduct = productsDao.getProduct(product.code).first()
            val updatedProduct = if(existingProduct != null) {
                product.copy(
                    isFavourite = existingProduct.isFavourite
                )
            } else product

            productsDao.upsertProduct(updatedProduct.toProductEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeProduct(code: String): EmptyResult<DataError.Local> {
        return try {
            productsDao.deleteProduct(code)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_ERROR)
        }
    }
}