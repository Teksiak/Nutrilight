package com.teksiak.nutrilight.core.domain.product

data class Nutriments(
    val energyKj: Int?,
    val energyKcal: Int?,
    val fat: Float?,
    val saturatedFat: Float?,
    val carbohydrates: Float?,
    val sugars: Float?,
    val fiber: Float?,
    val protein: Float?,
    val salt: Float?,
)
