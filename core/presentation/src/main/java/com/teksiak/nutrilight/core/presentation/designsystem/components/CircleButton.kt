package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack

@Composable
fun CircleButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = TintedBlack,
    onClick: () -> Unit = { }
) {
    OutlinedIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, ShadedWhite),
        modifier = modifier
            .size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleButtonPreview() {
    NutrilightTheme {
        CircleButton(
            icon = ScanBarIcon,
        )
    }
}