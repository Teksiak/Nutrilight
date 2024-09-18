package com.teksiak.nutrilight.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.teksiak.nutrilight.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Upsert
    suspend fun upsertProduct(favourite: ProductEntity)

    @Query("SELECT * FROM Products WHERE code = :code")
    fun getProduct(code: String): Flow<ProductEntity?>

    @Query("Delete FROM Products Where code = :code")
    suspend fun deleteProduct(code: String)
}