package com.teksiak.nutrilight.core.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProductDto(
    val code: String,
    val product: RemoteProduct?,
    val status: Int
)

@Serializable
data class RemoteProduct(
    val productName: String?,
    val selectedImages: RemoteSelectedImages?,
    val brands: String?,
    val quantity: String?,
    val packaging: String?,
    val novaGroup: Int?,
    val nutriments: RemoteNutriments?,
    val ecoscoreScore: Int?,
    val nutriscoreScore: Int?,
    val allergens: String?,
    val ingredients: List<RemoteIngredient>?
)

@Serializable
data class RemoteSelectedImages(
    val front: RemoteImagesCategory?
)

@Serializable
data class RemoteImagesCategory(
    val display: RemoteImages,
    val small: RemoteImages
)

@Serializable
data class RemoteImages(
    val de: String?,
    val en: String?,
    val fr: String?,
    val pl: String?,
    val es: String?,
    val it: String?,
)

@Serializable
data class RemoteIngredient (
    val id: String,
    val text: String
)

@Serializable
data class RemoteNutriments(
    @SerializedName("energy-kj_100g")
    val energyKj: Float?,
    @SerializedName("energy-kcal_100g")
    val energyKcal: Float?,
    @SerializedName("fat_100g")
    val fat100g: Float?,
    @SerializedName("saturated-fat_100g")
    val saturatedFat100g: Float?,
    @SerializedName("carbohydrates_100g")
    val carbohydrates100g: Float?,
    @SerializedName("sugars_100g")
    val sugars100g: Float?,
    @SerializedName("fiber_100g")
    val fiber100g: Float?,
    @SerializedName("proteins_100g")
    val proteins100g: Float?,
    @SerializedName("salt_100g")
    val salt100g: Float?,
)
