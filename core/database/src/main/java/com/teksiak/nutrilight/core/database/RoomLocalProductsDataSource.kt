@file:OptIn(ExperimentalCoroutinesApi::class)

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
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

    override fun getProductsHistory(): Flow<List<Product>> {
        return productsDao.getProductsHistory()
            .flatMapLatest { history ->
                productsDao.getProducts()
                    .map { productsList ->
                        history.mapNotNull { code ->
                            productsList.find { it.code == code }?.toProduct()
                        }
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

    override suspend fun removeFavourite(code: String): EmptyResult<DataError.Local> {
        return try {
            val isInHistory = productsDao.getProductsHistory().first().contains(code)
            if(isInHistory) {
                toggleFavourite(code)
            } else {
                productsDao.deleteProduct(code)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_ERROR)
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

            productsDao.addProduct(updatedProduct.toProductEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeProduct(code: String, ignoreHistory: Boolean): EmptyResult<DataError.Local> {
        return try {
            if(!ignoreHistory) {
                val isInHistory = productsDao.getProductsHistory().first().contains(code)
                if(isInHistory) return Result.Success(Unit)
            }
            productsDao.deleteProduct(code)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_ERROR)
        }
    }
}