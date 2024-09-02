package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.HeartFilledIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Silver

@Composable
fun FavouriteButton(
    size: Int = 24,
    isFavourite: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(size.dp),
        onClick = onToggle
    ) {
        Icon(
            imageVector = if(isFavourite) HeartFilledIcon else HeartIcon,
            contentDescription = if(isFavourite) "Remove from favourites" else "Add to favourites",
            tint = if(isFavourite) Color.Unspecified else Silver
        )
    }
}