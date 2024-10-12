package com.teksiak.nutrilight.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.teksiak.nutrilight.core.database.entity.ProductEntity
import com.teksiak.nutrilight.core.database.mapper.asHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao: HistoryDao {

    @Upsert
    suspend fun upsertProduct(product: ProductEntity)

    @Transaction
    suspend fun addProduct(product: ProductEntity, historySize: Int = 10) {
        upsertProduct(product)
        addToHistory(product.code.asHistoryEntity())
        if(getHistoryCount() > historySize) {
            deleteLastHistory()
        }
    }

    @Query("SELECT * FROM Products WHERE code = :code")
    fun getProduct(code: String): Flow<ProductEntity?>

    @Query("SELECT * FROM Products")
    fun getProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE Products SET isFavourite = NOT isFavourite WHERE code = :code")
    suspend fun toggleFavourite(code: String)

    @Query("Delete FROM Products Where code = :code")
    suspend fun deleteProduct(code: String)
}