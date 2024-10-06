package com.teksiak.nutrilight.core.network.mapper

import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.product.calculateScore
import com.teksiak.nutrilight.core.network.dto.RemoteIngredient
import com.teksiak.nutrilight.core.network.dto.RemoteNutriments
import com.teksiak.nutrilight.core.network.dto.ProductResultDto
import com.teksiak.nutrilight.core.network.dto.RemoteProduct
import com.teksiak.nutrilight.core.network.dto.RemoteSelectedImages

fun ProductResultDto.toProduct() = product!!.toProduct()

fun RemoteProduct.toProduct() = Product(
    code = code,
    name = productName ?: "",
    fullImageUrl = selectedImages.toFullImageUrl(),
    smallImageUrl = selectedImages.toSmallImageUrl(),
    brands = brands,
    quantity = quantity,
    packaging = packaging.removeLanguage(),
    novaGroup = novaGroup.toNovaGroup(),
    nutriments = nutriments.toNutriments(),
    score = calculateScore(nutriscoreScore, ecoscoreScore),
    allergens = allergens.removeLanguage(),
    ingredients = ingredients.toIngredients(),
)

// TODO: Get country from user settings and use it to get images
private fun RemoteSelectedImages?.toFullImageUrl(): String? {
    if (this == null) return null
    return front?.display?.run {
        pl ?: en ?: de ?: fr ?: es ?: it
    }
}

private fun RemoteSelectedImages?.toSmallImageUrl(): String? {
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