package com.teksiak.nutrilight.more.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightDialog

@Composable
fun HistorySizeDialog(
    selectedSizeIndex: Int,
    onHistorySizeSelect: (Int) -> Unit,
    onDismiss: () -> Unit = {}
) {
    NutrilightDialog(
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select_country),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(16.dp))
            VerticalPicker(
                items = SettingsRepository.HISTORY_SIZES,
                selectedIndex = selectedSizeIndex,
                onItemSelected = onHistorySizeSelect
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.history_size_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCountryDialog() {
    NutrilightTheme {
        HistorySizeDialog(
            selectedSizeIndex = 5,
            onHistorySizeSelect = {},
        )
    }
}