package com.teksiak.nutrilight.core.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun Modifier.topBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width

            drawLine(
                color = color,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = width , y = 0f),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width

            drawLine(
                color = color,
                start = Offset(x = 0f, y = size.height),
                end = Offset(x = width , y = size.height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)