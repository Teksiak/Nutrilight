package com.teksiak.nutrilight.more.util

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay

suspend fun simulatePress(
    interactionSource: MutableInteractionSource,
    delay: Long = 150,
    action: () -> Unit = {},
) {
    val press = PressInteraction.Press(Offset.Zero)
    interactionSource.emit(press)
    action()
    delay(delay)
    interactionSource.emit(PressInteraction.Release(press))
}