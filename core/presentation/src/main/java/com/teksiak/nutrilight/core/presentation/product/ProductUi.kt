package com.teksiak.nutrilight.core.presentation.product

import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Product

data class ProductUi(
    val code: String,
    val name: String,
    val brands: String,
    val quantity: String,
    val packaging: String,
    val novaGroup: NovaGroup?,
    val nutrimentsUi: NutrimentsUi?,
    val score: Float?,
    val allergens: String?,
    val ingredients: String,
    val ingredientsAmount: Int,
    val isFavourite: Boolean
)

fun Product.toProductUi() = ProductUi(
    code = code,
    name = name.formatName(brands, quantity, packaging),
    brands = brands.formatIfBlank(),
    quantity = quantity.formatIfBlank(),
    packaging = packaging.toFormattedPackaging(),
    novaGroup = novaGroup,
    nutrimentsUi = nutriments?.toNutrimentsUi(),
    score = score,
    allergens = allergens?.replaceFirstChar { it.uppercase() },
    ingredients = ingredients.joinToString(", "),
    ingredientsAmount = ingredients.filter { it.isNotBlank() }.size,
    isFavourite = isFavourite ?: false
)

private fun String?.formatName(brands: String?, quantity: String?, packaging: String?): String {
    if(isNullOrBlank()) {
        return "${brands ?: ""} ${packaging ?: ""} ${quantity ?: ""}"
    }
    return this
}

private fun String?.formatIfBlank(): String {
    if (isNullOrBlank()) return "-"
    return this
}

private fun String?.toFormattedPackaging(): String {
    if (isNullOrBlank()) return "-"
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