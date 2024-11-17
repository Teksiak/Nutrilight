package com.teksiak.nutrilight.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments

@Entity(tableName = "Products")
data class ProductEntity(
    @PrimaryKey val code: String,
    val name: String,
    val brands: String?,
    val fullImageUrl: String?,
    val smallImageUrl: String?,
    val quantity: String?,
    val packaging: String?,
    val novaGroup: NovaGroup?,
    @Embedded val nutriments: Nutriments?,
    val score: Float?,
    val allergens: String?,
    val ingredients: List<String>,
    val isFavourite: Boolean,
)