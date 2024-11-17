package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme

@Composable
fun NutrilightInformation(
    title: String,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            imageVector = LogoIcon,
            contentDescription = null,
            modifier = Modifier.size(
                height = 72.dp,
                width = 54.dp
            )
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun NutrilightInformationPreview() {
    NutrilightTheme {
        NutrilightInformation(
            title = "Nutrilight",
            description = AnnotatedString("Nutrilight is a nutrition app that helps you track your daily intake of nutrients.")
        )
    }
}