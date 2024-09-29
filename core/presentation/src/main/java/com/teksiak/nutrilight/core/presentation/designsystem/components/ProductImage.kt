package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.teksiak.nutrilight.core.presentation.R
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon

@Composable
fun ProductImage(
    modifier: Modifier = Modifier,
    loadingSize: Dp = 48.dp,
    imageUrl: String?
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.product_image),
        modifier = modifier,
        loading = {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation(
                    modifier = Modifier.size(loadingSize)
                )
            }
        },
        error = {
            Image(
                modifier = modifier
                    .fillMaxHeight()
                    .alpha(0.1f),
                imageVector = LogoRottenIcon,
                contentDescription = stringResource(id = R.string.product_image),
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(
                        0f
                    )
                })
            )
        },
        contentScale = ContentScale.Fit
    )
}