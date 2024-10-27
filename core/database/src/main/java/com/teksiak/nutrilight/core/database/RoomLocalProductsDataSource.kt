@file:OptIn(ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.core.database

import android.database.sqlite.SQLiteFullException
import com.teksiak.nutrilight.core.database.dao.ProductsDao
import com.teksiak.nutrilight.core.database.mapper.toProduct
import com.teksiak.nutrilight.core.database.mapper.toProductEntity
import com.teksiak.nutrilight.core.domain.LocalProductsDataSource
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class RoomLocalProductsDataSource(
    private val productsDao: ProductsDao,
    private val settingsRepository: SettingsRepository,
    private val applicationScope: CoroutineScope
): LocalProductsDataSource {

    private val historySize = MutableStateFlow(10)

    init {
        settingsRepository.historySize
            .onEach { newSize ->
                historySize.value = newSize
                correctHistorySize(newSize)
            }
            .launchIn(applicationScope)
    }

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

            productsDao.addProduct(updatedProduct.toProductEntity(), historySize.value)
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

    private suspend fun correctHistorySize(newSize: Int) {
        val oldSize = productsDao.getHistoryCount()
        if(newSize < oldSize) {
            val correction = oldSize - newSize
            productsDao.correctHistorySize(correction)
        }
    }
}