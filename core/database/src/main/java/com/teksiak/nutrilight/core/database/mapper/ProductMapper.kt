package com.teksiak.nutrilight.core.database.mapper

import com.teksiak.nutrilight.core.database.entity.ProductEntity
import com.teksiak.nutrilight.core.domain.product.Product

fun ProductEntity.toProduct() = Product(
    code = code,
    name = name,
    brands = brands,
    quantity = quantity,
    packaging = packaging,
    novaGroup = novaGroup,
    nutriments = nutriments,
    score = score,
    allergens = allergens,
    ingredients = ingredients,
    isFavourite = isFavourite
)

fun Product.toProductEntity() = ProductEntity(
    code = code,
    name = name,
    brands = brands,
    quantity = quantity,
    packaging = packaging,
    novaGroup = novaGroup,
    nutriments = nutriments,
    score = score,
    allergens = allergens,
    ingredients = ingredients,
    isFavourite = isFavourite
)