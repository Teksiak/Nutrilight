package com.teksiak.nutrilight.core.domain.product

data class Product(
    val code: String,
    val name: String,
    val imageUrl: String?,
    val brands: String,
    val quantity: String,
    val packaging: String?,
    val novaGroup: NovaGroup?,
    val nutriments: Nutriments,
    val score: Float?,
    val allergens: List<String>,
    val ingredients: List<String>,
)
