package com.teksiak.nutrilight.core.presentation.product

import com.teksiak.nutrilight.core.domain.product.Nutriments
import kotlin.math.roundToInt

data class NutrimentsUi(
    val areNutrimentsComplete: Boolean,
    val roundedEnergyKcal: String,
    val roundedProtein: String,
    val roundedFat: String,
    val roundedCarbohydrates: String,
    val proteinFraction: Float,
    val fatFraction: Float,
    val carbohydratesFraction: Float,
    val energy: String,
    val fat: String,
    val saturatedFat: String,
    val carbohydrates: String,
    val sugars: String,
    val fiber: String,
    val protein: String,
    val salt: String
)

fun Nutriments.toNutrimentsUi(): NutrimentsUi {
    val areNutrimentsComplete =
        energyKcal != null && (protein != null || fat != null || carbohydrates != null)

    val proteinFraction = if (areNutrimentsComplete && this.energyKcal != 0f) {
        (protein ?: 0f) * 4f / (this.energyKcal!!)
    } else 0f

    val fatFraction = if (areNutrimentsComplete && this.energyKcal != 0f) {
        (fat ?: 0f) * 9f / (this.energyKcal!!)
    } else 0f

    val carbohydratesFraction = if (areNutrimentsComplete && this.energyKcal != 0f) {
        (carbohydrates ?: 0f) * 4f / (this.energyKcal!!)
    } else 0f

    return NutrimentsUi(
        areNutrimentsComplete = areNutrimentsComplete,
        roundedEnergyKcal = energyKcal.roundNutrimentToString(),
        roundedProtein = protein.roundNutrimentToGrams(),
        roundedFat = fat.roundNutrimentToGrams(),
        roundedCarbohydrates = carbohydrates.roundNutrimentToGrams(),
        proteinFraction = proteinFraction,
        fatFraction = fatFraction,
        carbohydratesFraction = carbohydratesFraction,
        energy = formatEnergy(energyKj, energyKcal),
        fat = (fat ?: 0f).formatToGrams(),
        saturatedFat = saturatedFat.formatToGrams(),
        carbohydrates = (carbohydrates ?: 0f).formatToGrams(),
        sugars = sugars.formatToGrams(),
        fiber = fiber.formatToGrams(),
        protein = (protein ?: 0f).formatToGrams(),
        salt = salt.formatToGrams(),
    )
}

private fun Float?.roundNutrimentToString(): String {
    return this?.let { "${it.roundToInt()}" } ?: "-"
}

private fun Float?.roundNutrimentToGrams(): String {
    return this?.let { "${it.roundToInt()} g" } ?: "0 g"
}

private fun formatEnergy(energyKj: Float?, energyKcal: Float?): String {
    return if (energyKj != null && energyKcal != null) {
        "${"%,d".format(energyKj.toInt())} kj (${energyKcal.formatNumber()} kcal)"
    } else if(energyKcal != null) {
        "${energyKcal.formatNumber()} kcal"
    } else {
        "- kcal"
    }
}

private fun Float?.formatToGrams(): String {
    return this?.let { "${it.formatNumber() } g" } ?: "- g"
}

private fun Float.formatNumber(): String {
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}