package com.teksiak.nutrilight.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.HeartXIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.PrimaryButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.SecondaryButton
import com.teksiak.nutrilight.product.presentation.R

@Composable
fun RemoveFavouriteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    NutrilightDialog(
        title = stringResource(id = R.string.oh_no),
        description = stringResource(id = R.string.remove_favourite_confirmation),
        onDismiss = onDismiss,
        icon = {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = HeartXIcon,
                contentDescription = null,
                tint = Primary
            )
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SecondaryButton(
                    modifier = Modifier.width(128.dp),
                    text = stringResource(id = R.string.cancel),
                    onClick = onDismiss,
                    color = TintedBlack
                )
                PrimaryButton(
                    modifier = Modifier.width(128.dp),
                    text = stringResource(id = R.string.remove),
                    onClick = onConfirm,
                )
            }
        }
    )
}