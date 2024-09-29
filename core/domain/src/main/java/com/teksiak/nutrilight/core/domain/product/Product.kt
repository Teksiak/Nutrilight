package com.teksiak.nutrilight.core.domain.product

data class Product(
    val code: String,
    val name: String,
    val fullImageUrl: String? = null,
    val smallImageUrl: String? = null,
    val brands: String?,
    val quantity: String?,
    val packaging: String?,
    val novaGroup: NovaGroup?,
    val nutriments: Nutriments?,
    val score: Float?,
    val allergens: String?,
    val ingredients: List<String>,
    val isFavourite: Boolean? = null,
)
