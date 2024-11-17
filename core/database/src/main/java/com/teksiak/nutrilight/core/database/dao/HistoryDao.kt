package com.teksiak.nutrilight.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.teksiak.nutrilight.core.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Upsert
    suspend fun addToHistory(history: HistoryEntity)

    @Query("SELECT code FROM History ORDER BY timestamp DESC")
    fun getProductsHistory(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM History")
    suspend fun getHistoryCount(): Int

    @Transaction
    suspend fun deleteLastHistory() {
        val code = getProductsToCorrectHistorySize(1).first()
        deleteFromHistory(code)
        deleteNotFavouriteProduct(code)
    }

    @Transaction
    suspend fun correctHistorySize(correction: Int) {
        val productsToCorrect = getProductsToCorrectHistorySize(correction)
        productsToCorrect.forEach { code ->
            removeFromHistory(code)
        }
    }

    @Query("SELECT code FROM History ORDER BY timestamp ASC LIMIT :correction")
    suspend fun getProductsToCorrectHistorySize(correction: Int): List<String>

    @Transaction
    suspend fun removeFromHistory(code: String) {
        deleteFromHistory(code)
        deleteNotFavouriteProduct(code)
    }

    @Query("DELETE FROM History WHERE code = :code")
    suspend fun deleteFromHistory(code: String)

    @Query("DELETE FROM Products WHERE code = :code AND isFavourite = 0")
    suspend fun deleteNotFavouriteProduct(code: String)

}