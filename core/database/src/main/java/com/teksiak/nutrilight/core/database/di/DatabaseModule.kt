package com.teksiak.nutrilight.core.database.di

import android.content.Context
import androidx.room.Room
import com.teksiak.nutrilight.core.database.ProductsDatabase
import com.teksiak.nutrilight.core.database.RoomLocalProductsDataSource
import com.teksiak.nutrilight.core.database.dao.ProductsDao
import com.teksiak.nutrilight.core.domain.LocalProductsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProductsDatabase(
        @ApplicationContext context: Context
    ): ProductsDatabase {
        return Room.databaseBuilder(
            context,
            ProductsDatabase::class.java,
            "product.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductsDao(
        productsDatabase: ProductsDatabase
    ) = productsDatabase.productsDao

    @Provides
    @Singleton
    fun provideProductsDataSource(
        productsDao: ProductsDao
    ): LocalProductsDataSource = RoomLocalProductsDataSource(productsDao)
}