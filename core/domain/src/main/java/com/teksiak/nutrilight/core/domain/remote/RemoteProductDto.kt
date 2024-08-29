package com.teksiak.nutrilight.core.domain.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProductDto(
    val code: String,
    val product: RemoteProduct,
)

@Serializable
data class RemoteProduct(
    val productName: String,
    val brands: String?,
    val quantity: String?,
    val packaging: String?,
    val novaGroup: Int?,
    val nutriments: RemoteNutriments,
    val ecoscoreScore: Int?,
    val nutriscoreScore: Int?,
    val allergens: String?,
    val ingredients: List<RemoteIngredient>
    )

@Serializable
data class RemoteIngredient (
    val id: String,
    val text: String
)

@Serializable
data class RemoteNutriments(
    @SerialName("energy-kj_100g")
    val energyKj: Int?,
    @SerialName("energy-kcal_100g")
    val energyKcal: Int?,
    val fat100g: Float?,
    val saturatedFat100g: Float?,
    val carbohydrates100g: Float?,
    val sugars100g: Float?,
    val fiber100g: Float?,
    val proteins100g: Float?,
    val salt100g: Float?,
)
