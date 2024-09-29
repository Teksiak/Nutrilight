package com.teksiak.nutrilight.core.data.mapper

import com.teksiak.nutrilight.core.data.RemoteIngredient
import com.teksiak.nutrilight.core.data.RemoteNutriments
import com.teksiak.nutrilight.core.data.RemoteProductDto
import com.teksiak.nutrilight.core.data.RemoteSelectedImages
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.product.calculateScore

fun RemoteProductDto.toProduct() = Product(
    code = code,
    name = product!!.productName ?: "",
    fullImageUrl = product.selectedImages.toFullImageUrl(),
    smallImageUrl = product.selectedImages.toThumbImageUrl(),
    brands = product.brands,
    quantity = product.quantity,
    packaging = product.packaging.removeLanguage(),
    novaGroup = product.novaGroup.toNovaGroup(),
    nutriments = product.nutriments.toNutriments(),
    score = calculateScore(product.nutriscoreScore, product.ecoscoreScore),
    allergens = product.allergens.removeLanguage(),
    ingredients = product.ingredients.toIngredients(),
)

// TODO: Get country from user settings and use it to get images
private fun RemoteSelectedImages?.toFullImageUrl(): String? {
    if (this == null) return null
    return front?.display?.run {
        pl ?: en ?: de ?: fr ?: es ?: it
    }
}

private fun RemoteSelectedImages?.toThumbImageUrl(): String? {
    if (this == null) return null
    return front?.small?.run {
        pl ?: en ?: de ?: fr ?: es ?: it
    }
}

private fun String?.removeLanguage(): String? {
    if (this == null) return null
    return this.split(",").joinToString(separator = ", ") { packaging ->
        packaging.takeLastWhile { it != ':' }
    }
}

private fun Int?.toNovaGroup(): NovaGroup? = when (this) {
    1 -> NovaGroup.NOVA_1
    2 -> NovaGroup.NOVA_2
    3 -> NovaGroup.NOVA_3
    4 -> NovaGroup.NOVA_4
    else -> null
}

fun RemoteNutriments?.toNutriments(): Nutriments? {
    if (this == null) return null
    return Nutriments(
        energyKj = energyKj,
        energyKcal = energyKcal,
        fat = fat100g,
        saturatedFat = saturatedFat100g,
        carbohydrates = carbohydrates100g,
        sugars = sugars100g,
        fiber = fiber100g,
        protein = proteins100g,
        salt = salt100g,
    )
}

private fun List<RemoteIngredient>?.toIngredients(): List<String> {
    if(this == null) return emptyList()
    return this.map { it.text }
}