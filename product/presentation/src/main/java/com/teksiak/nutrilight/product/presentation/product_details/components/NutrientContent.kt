package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.presentation.designsystem.AlmostWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Carbs
import com.teksiak.nutrilight.core.presentation.designsystem.ChevronDownIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ChevronUpIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Fat
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Protein
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.product.presentation.product_details.util.DummyProduct
import kotlin.math.roundToInt

@Composable
fun NutrientContent(
    nutriments: Nutriments?,
    modifier: Modifier = Modifier,
    showNutritionFacts: Boolean = false,
    toggleNutritionFacts: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if(areNutrimentsComplete(nutriments)) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                toggleNutritionFacts()
                            }
                        )
                    }
                } else {
                    val textMeasurer = rememberTextMeasurer()
                    val textStyle = MaterialTheme.typography.bodyLarge

                    val textLayoutResult: TextLayoutResult =
                        textMeasurer.measure(
                            text = "No available data",
                            style = textStyle
                    )
                    val textSize = textLayoutResult.size

                    Modifier
                        .drawWithContent {
                            drawContent()
                            drawText(
                                textMeasurer = textMeasurer,
                                text = "No available data",
                                style = textStyle,
                                topLeft = Offset(
                                    x = (size.width - textSize.width) / 2,
                                    y = (size.height - textSize.height) / 2,
                                ),
                            )
                        }
                        .blur(
                            3.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .alpha(0.7f)
                }
            )
    ) {
        Text(
            text = stringResource(R.string.in_100g),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CaloriesCircle(nutriments)
            Spacer(modifier = Modifier.size(16.dp))
            NutrimentsBars(
                nutriments = nutriments,
                modifier = Modifier.fillMaxHeight()
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = if(showNutritionFacts) {
                    ChevronUpIcon
                } else {
                    ChevronDownIcon
                },
                tint = Silver,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(64.dp),
            text = stringResource(R.string.calories),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NutrimentsBars(
    nutriments: Nutriments?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if(areNutrimentsComplete(nutriments)) {
            val proteinPart: Float = nutriments!!.protein!! * 4f / (nutriments.energyKcal!!)
            val fatPart: Float = nutriments.fat!! * 9f / (nutriments.energyKcal!!)
            val carbohydratesPart: Float = nutriments.carbohydrates!! * 4f / (nutriments.energyKcal!!)

            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.protein),
                value = nutriments.protein?.roundToInt().toString() + " g",
                color = Protein,
                part = proteinPart
            )
            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.fat),
                value = nutriments.fat?.roundToInt().toString() + " g",
                color = Fat,
                part = fatPart
            )
            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.carbs),
                value = nutriments.carbohydrates?.roundToInt().toString() + " g",
                color = Carbs,
                part = carbohydratesPart
            )
        } else {
            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.protein),
                value = "-",
                color = Protein,
            )
            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.fat),
                value = "-",
                color = Fat
            )
            NutrimentBar(
                modifier = Modifier.width(IntrinsicSize.Max),
                label = stringResource(R.string.carbs),
                value = "-",
                color = Carbs
            )
        }
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

@Composable
private fun CaloriesCircle(
    nutriments: Nutriments?
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
            if(areNutrimentsComplete(nutriments)) {
                val proteinPart: Pair<Float, Float> = -90f to 360f * nutriments!!.protein!! * 4f / (nutriments.energyKcal!!)
                val fatPart: Pair<Float, Float> = proteinPart.second - 90f to 360f * nutriments.fat!! * 9f / (nutriments.energyKcal!!)
                val carbohydratesPart: Pair<Float, Float> = fatPart.second + fatPart.first to 360f * nutriments.carbohydrates!! * 4f / (nutriments.energyKcal!!)

                drawArc(
                    color = Protein,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = proteinPart.first,
                    sweepAngle = proteinPart.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Fat,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = fatPart.first,
                    sweepAngle = fatPart.second,
                    useCenter = false,
                    style = circleStyle
                )
                drawArc(
                    color = Carbs,
                    size = Size(circleRadius * 2, circleRadius * 2),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    startAngle = carbohydratesPart.first,
                    sweepAngle = carbohydratesPart.second,
                    useCenter = false,
                    style = circleStyle
                )
            }
        }
        Text(
            text = nutriments?.energyKcal?.roundToInt().toString().takeIf { it != "null" } ?: "-",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun areNutrimentsComplete(nutriments: Nutriments?): Boolean {
    return nutriments?.energyKcal != null
            && nutriments.protein != null
            && nutriments.fat != null
            && nutriments.carbohydrates != null
}

@Preview(showBackground = true)
@Composable
private fun NutrientContentPreview() {
    NutrilightTheme {
        NutrientContent(
            nutriments = DummyProduct.nutriments
        )
    }
}