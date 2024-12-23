package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Silver

@Composable
fun InteractionDialog(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isDismissible: Boolean = true,
    onDismiss: () -> Unit = {},
    icon: @Composable () -> Unit = {},
    buttons: @Composable () -> Unit = {},
    bottomText: String? = null,
    onBackPressed: (() -> Unit)? = null
) {
    NutrilightDialog(
        modifier = modifier,
        paddingValues = PaddingValues(
            top = 24.dp,
            start = 24.dp,
            end = 24.dp,
            bottom = if (bottomText != null) 8.dp else 24.dp
        ),
        isDismissible = isDismissible,
        onDismiss = onDismiss,
        onBackPressed = onBackPressed
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            buttons()
            if (bottomText != null) {
                Text(
                    text = bottomText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Silver
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun NutrilightDialogPreview() {
    NutrilightTheme {
        InteractionDialog(
            title = "Whoops",
            description = "We were unable to find this product.",
            icon = {
                Icon(
                    imageVector = LogoRottenIcon,
                    contentDescription = "Error",
                    tint = Color.Unspecified
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SecondaryButton(
                        modifier = Modifier.weight(1f),
                        text = "Try again",
                        onClick = { }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    PrimaryButton(
                        modifier = Modifier.weight(1f),
                        text = "Search",
                        onClick = { }
                    )
                }
            },
            bottomText = "Scanned ID: 59024092702453"
        )
    }
}