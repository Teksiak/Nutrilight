package com.teksiak.nutrilight.core.presentation.util

import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product

val DummyProduct = Product(
    code = "20724696",
    name = "Californian Almond test",
    brands = "Alesto,Lidl,Solent",
    quantity = "200g",
    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
    novaGroup = NovaGroup.NOVA_1,
    nutriments = Nutriments(
        energyKj = 2567f,
        energyKcal = 621f,
        fat = 53.3f,
        saturatedFat = 4.3f,
        carbohydrates = 4.8f,
        sugars = 4.8f,
        fiber = 12.1f,
        protein = 24.5f,
        salt = 0.01f
    ),
    allergens = "Nuts",
    ingredients = listOf(
        "almonds"
    ),
    score = 4.6f
)