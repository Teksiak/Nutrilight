package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.AlmostWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Carbs
import com.teksiak.nutrilight.core.presentation.designsystem.Fat
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Protein
import com.teksiak.nutrilight.core.presentation.product.NutrimentsUi
import com.teksiak.nutrilight.core.presentation.product.toNutrimentsUi
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.core.presentation.util.DummyProduct

@Composable
fun NutrimentsBarChart(
    nutrimentsUi: NutrimentsUi?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val proteinFraction: Float = nutrimentsUi?.proteinFraction ?: 0f
        val fatFraction: Float = nutrimentsUi?.fatFraction ?: 0f
        val carbohydratesFraction: Float = nutrimentsUi?.carbohydratesFraction ?: 0f

        NutrimentBar(
            modifier = Modifier.width(IntrinsicSize.Max),
            label = stringResource(R.string.protein),
            value = nutrimentsUi?.roundedProtein ?: "-",
            color = Protein,
            part = proteinFraction
        )
        NutrimentBar(
            modifier = Modifier.width(IntrinsicSize.Max),
            label = stringResource(R.string.fat),
            value = nutrimentsUi?.roundedFat ?: "-",
            color = Fat,
            part = fatFraction
        )
        NutrimentBar(
            modifier = Modifier.width(IntrinsicSize.Max),
            label = stringResource(R.string.carbs),
            value = nutrimentsUi?.roundedCarbohydrates ?: "-",
            color = Carbs,
            part = carbohydratesFraction
        )
    }
}

@Composable
private fun NutrimentBar(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    part: Float = 0f
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.width(48.dp),
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Canvas(
                modifier = Modifier.width(120.dp)
            ) {
                drawRoundRect(
                    color = AlmostWhite,
                    topLeft = Offset(0f, size.height / 2 - 3.5.dp.toPx()),
                    size = Size(120.dp.toPx(), 8.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )

                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, size.height / 2 - 3.5.dp.toPx()),
                    size = Size(120.dp.toPx() * part, 8.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NutrimentsBarChartPreview() {
    NutrilightTheme {
        NutrimentsBarChart(
            nutrimentsUi = DummyProduct.nutriments?.toNutrimentsUi()
        )
    }
}