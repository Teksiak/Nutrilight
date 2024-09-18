package com.teksiak.nutrilight.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teksiak.nutrilight.core.database.dao.ProductsDao
import com.teksiak.nutrilight.core.database.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProductsDatabase : RoomDatabase() {
    abstract val productsDao: ProductsDao
}