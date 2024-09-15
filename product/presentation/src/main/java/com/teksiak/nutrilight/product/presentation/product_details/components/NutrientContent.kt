package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.ChevronDownIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ChevronUpIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.product.NutrimentsUi
import com.teksiak.nutrilight.core.presentation.product.toNutrimentsUi
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.core.presentation.util.DummyProduct

@Composable
fun NutrientContent(
    nutrimentsUi: NutrimentsUi?,
    modifier: Modifier = Modifier,
    showNutritionFacts: Boolean = false,
    toggleNutritionFacts: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if(nutrimentsUi?.areNutrimentsComplete == true) {
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
            NutrimentsPieChart(
                nutrimentsUi = nutrimentsUi,
            )
            Spacer(modifier = Modifier.size(16.dp))
            NutrimentsBarChart(
                nutrimentsUi = nutrimentsUi,
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


@Preview(showBackground = true)
@Composable
private fun NutrientContentPreview() {
    NutrilightTheme {
        NutrientContent(
            nutrimentsUi = DummyProduct.nutriments?.toNutrimentsUi()
        )
    }
}