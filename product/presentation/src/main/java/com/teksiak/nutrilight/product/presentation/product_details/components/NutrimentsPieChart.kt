package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.AlmostWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Carbs
import com.teksiak.nutrilight.core.presentation.designsystem.Fat
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Protein
import com.teksiak.nutrilight.core.presentation.product.NutrimentsUi
import com.teksiak.nutrilight.core.presentation.product.toNutrimentsUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct

@Composable
fun NutrimentsPieChart(
    nutrimentsUi: NutrimentsUi?,
) {
    Box(
        modifier = Modifier.size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(64.dp)
        ) {
            val circleStyle = Stroke(width = 8.dp.toPx())
            val circleRadius = (size.width - 8.dp.toPx()) / 2

            drawCircle(
                color = AlmostWhite,
                radius = circleRadius,
                style = circleStyle
            )
            if(nutrimentsUi?.areNutrimentsComplete == true) {
                val proteinFraction: Pair<Float, Float> = -90f to 360f * nutrimentsUi.proteinFraction
                val fatFraction: Pair<Float, Float> = proteinFraction.second - 90f to 360f * nutrimentsUi.fatFraction
                val carbohydratesFraction: Pair<Float, Float> = fatFraction.second + fatFraction.first to 360f * nutrimentsUi.carbohydratesFraction

                drawArc(
                    color = Protein,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = proteinFraction.first,
                    sweepAngle = proteinFraction.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Fat,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = fatFraction.first,
                    sweepAngle = fatFraction.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Carbs,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = carbohydratesFraction.first,
                    sweepAngle = carbohydratesFraction.second,
                    useCenter = false,
                    style = circleStyle
                )
            }
        }
        Text(
            text = nutrimentsUi?.roundedEnergyKcal ?: "-",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun NutrimentsPieChartPreview() {
    NutrilightTheme {
        NutrimentsPieChart(
            nutrimentsUi = DummyProduct.nutriments?.toNutrimentsUi()
        )
    }
}