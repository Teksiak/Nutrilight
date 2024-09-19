package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.BackIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.util.bottomBorder

@Composable
fun NutrilightAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    actionButtons: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bottomBorder(1.dp, ShadedWhite)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = BackIcon,
                contentDescription = "Back",
                tint = TintedBlack
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            softWrap = true,
        )
        actionButtons()
    }
}