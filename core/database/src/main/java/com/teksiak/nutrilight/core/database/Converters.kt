package com.teksiak.nutrilight.core.database

import androidx.room.TypeConverter
import com.teksiak.nutrilight.core.domain.product.NovaGroup

class Converters {
    @TypeConverter
    fun toNova(value: String?): NovaGroup? {
        return value?.let { NovaGroup.valueOf(it) }
    }

    @TypeConverter
    fun fromNova(novaGroup: NovaGroup?): String? {
        return novaGroup?.name
    }

    @TypeConverter
    fun ingredientsToString(ingredients: List<String>): String {
        return ingredients.joinToString(",")
    }

    @TypeConverter
    fun stringToIngredients(ingredients: String): List<String> {
        return ingredients.split(",")
    }

}