package com.teksiak.nutrilight.core.presentation.ui_models

import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Product

data class ProductUi(
    val code: String,
    val name: String,
    val fullImageUrl: String?,
    val smallImageUrl: String?,
    val brands: String,
    val quantity: String,
    val packaging: String,
    val novaGroup: NovaGroup?,
    val nutrimentsUi: NutrimentsUi?,
    val score: Float?,
    val allergens: String?,
    val ingredients: String,
    val ingredientsAmount: Int,
    val isFavourite: Boolean,
    val showImage: Boolean
)

fun Product.toProductUi(showImage: Boolean = true) = ProductUi(
    code = code,
    name = name.formatName(brands, quantity, packaging),
    fullImageUrl = fullImageUrl,
    smallImageUrl = smallImageUrl,
    brands = brands.formatIfBlank(),
    quantity = quantity.formatIfBlank(),
    packaging = packaging.toFormattedPackaging(),
    novaGroup = novaGroup,
    nutrimentsUi = nutriments?.toNutrimentsUi(),
    score = score,
    allergens = allergens?.replaceFirstChar { it.uppercase() },
    ingredients = ingredients.joinToString(", "),
    ingredientsAmount = ingredients.filter { it.isNotBlank() }.size,
    isFavourite = isFavourite ?: false,
    showImage = showImage
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