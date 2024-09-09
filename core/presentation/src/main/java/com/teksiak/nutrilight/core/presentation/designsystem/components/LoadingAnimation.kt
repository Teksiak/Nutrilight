package com.teksiak.nutrilight.core.presentation.designsystem.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.teksiak.nutrilight.core.presentation.R

@Composable
fun LoadingAnimation() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(data = R.drawable.loading)
                .apply {
                    size(Size.ORIGINAL)
                }
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = null
    )
}