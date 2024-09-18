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

    override suspend fun upsertProduct(product: Product): Result<Unit, DataError.Local> {
        return try {
            productsDao.upsertProduct(product.toProductEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
}