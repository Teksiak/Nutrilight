package com.teksiak.nutrilight.core.domain.product

import kotlin.math.abs

fun calculateScore(nutriscore: Int?, ecoscore: Int?): Float? {
    if(nutriscore == null || ecoscore == null) return null
    val nutriscoreFactor = abs(-40 + nutriscore) / 55f * 44f
    val ecoscoreFactor = abs(0 - ecoscore) / 100f * 80f
    return 1f + 4f * (nutriscoreFactor * 0.9f + ecoscoreFactor * 0.1f)
}
