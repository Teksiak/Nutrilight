package com.teksiak.nutrilight.core.data.mapper

import com.teksiak.nutrilight.core.data.RemoteIngredient
import com.teksiak.nutrilight.core.data.RemoteNutriments
import com.teksiak.nutrilight.core.data.RemoteProductDto
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.product.calculateScore

fun RemoteProductDto.toProduct() = Product(
    code = code,
    name = product.productName!!,
    brands = product.brands,
    quantity = product.quantity,
    packaging = product.packaging.toFormattedPackaging(),
    novaGroup = product.novaGroup.toNovaGroup(),
    nutriments = product.nutriments.toNutriments(),
    score = calculateScore(product.nutriscoreScore, product.ecoscoreScore),
    allergens = product.allergens.toAllergensList(),
    ingredients = product.ingredients.toIngredients(),
)

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

fun Int?.toNovaGroup(): NovaGroup? = when (this) {
    1 -> NovaGroup.NOVA_1
    2 -> NovaGroup.NOVA_2
    3 -> NovaGroup.NOVA_3
    4 -> NovaGroup.NOVA_4
    else -> null
}

fun String?.toFormattedPackaging(): String {
    if (this == null) return ""
    return this.split(",").joinToString(", ") { packaging ->
        packaging
            .takeLastWhile {
                it != ':'
            }
            .replaceFirstChar {
                it.uppercase()
            }
    }
}

fun String?.toAllergensList(): List<String> {
    if (this == null) return emptyList()
    return this.split(",").map { allergen ->
        allergen
            .takeLastWhile {
                it != ':'
            }
            .replaceFirstChar {
                it.uppercase()
            }
    }
}

fun List<RemoteIngredient>?.toIngredients(): List<String> {
    if(this == null) return emptyList()
    return this.map { it.text }
}