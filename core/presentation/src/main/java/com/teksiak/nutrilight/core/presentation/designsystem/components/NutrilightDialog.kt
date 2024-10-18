package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.designsystem.XCloseIcon

@Composable
fun NutrilightDialog(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(24.dp),
    isDismissible: Boolean = true,
    onDismiss: () -> Unit = {},
    onBackPressed: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnClickOutside = isDismissible,
            dismissOnBackPress = onBackPressed != null || isDismissible
        )
    ) {
        BackHandler(enabled = onBackPressed != null) {
            onBackPressed?.invoke()
        }

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(White)
                .padding(paddingValues),
            contentAlignment = Alignment.TopEnd
        ) {
            if (isDismissible) {
                IconButton(
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 12.dp, y = (-12).dp),
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = XCloseIcon,
                        contentDescription = "Close dialog",
                        tint = Silver
                    )
                }
            }

            content()
        }
    }
}
