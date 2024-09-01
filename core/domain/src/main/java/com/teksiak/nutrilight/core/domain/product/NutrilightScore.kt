package com.teksiak.nutrilight.core.domain.product

import kotlin.math.abs
import kotlin.math.round

fun calculateScore(nutriscore: Int?, ecoscore: Int?): Float? {
    if(nutriscore == null || ecoscore == null) return null
    val nutriscoreFactor = abs(-40 + nutriscore) / 44f
    val ecoscoreFactor = abs(0 - ecoscore) / 80f
    return  round((1f + 4f * nutriscoreFactor * 0.9f + 4f * ecoscoreFactor * 0.1f) * 10f) / 10f
}
