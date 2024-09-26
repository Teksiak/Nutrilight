package com.teksiak.nutrilight.core.database.dao

import androidx.room.Dao
import androidx.room.Query
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

    @Query("DELETE FROM History WHERE code = :code")
    suspend fun deleteFromHistory(code: String)

    @Query("DELETE FROM history WHERE code = (SELECT code FROM history ORDER BY timestamp DESC LIMIT 1)")
    suspend fun deleteLastHistory()

    @Query("DELETE FROM History")
    suspend fun clearHistory()

}