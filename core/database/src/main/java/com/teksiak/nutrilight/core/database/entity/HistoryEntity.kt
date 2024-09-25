package com.teksiak.nutrilight.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "History",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["code"],
            childColumns = ["code"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistoryEntity(
    @PrimaryKey val code: String,
    val timestamp: Long = System.currentTimeMillis()
)
