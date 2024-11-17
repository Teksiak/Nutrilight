package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teksiak.nutrilight.core.presentation.designsystem.HeartFilledIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Silver

@Composable
fun FavouriteAnimatedIcon(
    isFavourite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    defaultColor: Color = Silver
) {
    AnimatedContent(
        targetState = isFavourite,
        transitionSpec = {
            (fadeIn(
                animationSpec = tween(
                    150,
                    delayMillis = 60
                )
            ) + scaleIn(
                initialScale = 0.3f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )).togetherWith(
                fadeOut(animationSpec = tween(90))
                        + scaleOut(
                    targetScale = 0.3f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            )
        },
        label = ""
    ) { favouriteStatus ->
        Icon(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onClick()
                },
            imageVector = if (favouriteStatus) HeartFilledIcon else HeartIcon,
            contentDescription = null,
            tint = if (favouriteStatus) Color.Unspecified else defaultColor
        )
    }
}