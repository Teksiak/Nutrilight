package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.product.NutrimentsUi
import com.teksiak.nutrilight.core.presentation.product.toNutrimentsUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.product.presentation.R

@Composable
fun NutritionFacts(
    nutrimentsUi: NutrimentsUi?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        LabelRow(
            label = stringResource(R.string.nutrition_facts),
            value = stringResource(R.string.in_100g),
            isHeader = true
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.energy),
            value = nutrimentsUi?.energy ?: "- kcal"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.fat),
            value = nutrimentsUi?.fat ?: "- g"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.saturated_fat),
            value = nutrimentsUi?.saturatedFat ?: "- g",
            isSubRow = true
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.carbohydrates),
            value = nutrimentsUi?.carbohydrates ?: "- g"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.sugars),
            value = nutrimentsUi?.sugars ?: "- g",
            isSubRow = true
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.fiber),
            value = nutrimentsUi?.fiber ?: "- g",
            isSubRow = true
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.protein),
            value = nutrimentsUi?.protein ?: "- g"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = ShadedWhite
        )
        LabelRow(
            label = stringResource(R.string.salt),
            value = nutrimentsUi?.salt ?: "- g"
        )
    }
}

@Composable
private fun LabelRow(
    label: String,
    value: String,
    isHeader: Boolean = false,
    isSubRow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = if(isSubRow) 12.dp else 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if(isHeader) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = value,
            style = if(isHeader) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutrimentsLabelPreview() {
    NutrilightTheme {
        NutritionFacts(
            nutrimentsUi = DummyProduct.nutriments?.toNutrimentsUi()
        )
    }
}