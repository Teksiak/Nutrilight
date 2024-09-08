package com.teksiak.nutrilight.scanner.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.scanner.presentation.R

@Composable
fun ScannerOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val lineOpacity by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scan line opacity"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = TintedBlack.copy(alpha = 0.4f),
                    topLeft = Offset(0f, 0f),
                    size = size,
                )
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(
                        (size.width - 238.dp.toPx()) / 2,
                        (size.height - 154.dp.toPx()) / 2
                    ),
                    size = Size(238.dp.toPx(), 154.dp.toPx()),
                    cornerRadius = CornerRadius(40.dp.toPx()),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .width(248.dp)
                .height(160.dp),
            painter = painterResource(id = R.drawable.scan_borders),
            contentDescription = null
        )
        HorizontalDivider(
            modifier = Modifier
                .width(200.dp)
                .clip(RoundedCornerShape(3.dp))
                .alpha(lineOpacity),
            thickness = 3.dp,
            color = Primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScannerOverlayPreview() {
    ScannerOverlay()
}