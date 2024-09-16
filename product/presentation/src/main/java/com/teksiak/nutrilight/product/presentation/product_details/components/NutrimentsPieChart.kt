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
import androidx.compose.ui.res.painterResource
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
    proteinFraction: Float? = null,
    fatFraction: Float? = null,
    carbohydratesFraction: Float? = null
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
                val proteinChartData: Pair<Float, Float> = -90f to 360f * (proteinFraction ?: nutrimentsUi.proteinFraction)
                val fatChartData: Pair<Float, Float> = proteinChartData.second - 90f to 360f * (fatFraction ?: nutrimentsUi.fatFraction)
                val carbohydratesChartData: Pair<Float, Float> = fatChartData.second + fatChartData.first to 360f * (carbohydratesFraction ?: nutrimentsUi.carbohydratesFraction)

                drawArc(
                    color = Protein,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = proteinChartData.first,
                    sweepAngle = proteinChartData.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Fat,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = fatChartData.first,
                    sweepAngle = fatChartData.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Carbs,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = carbohydratesChartData.first,
                    sweepAngle = carbohydratesChartData.second,
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

@Preview(showBackground = true)
@Composable
private fun NutrimentsPieChartPreview() {
    NutrilightTheme {
        NutrimentsPieChart(
            nutrimentsUi = DummyProduct.nutriments?.toNutrimentsUi()
        )
    }
}