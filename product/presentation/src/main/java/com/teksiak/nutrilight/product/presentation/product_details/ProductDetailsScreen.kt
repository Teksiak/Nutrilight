@file:OptIn(ExperimentalMaterial3Api::class)

package com.teksiak.nutrilight.product.presentation.product_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.designsystem.BackIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScore
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.product.presentation.product_details.components.NutrientContent
import com.teksiak.nutrilight.product.presentation.product_details.components.ProductBasicInformation
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.core.presentation.util.bottomBorder
import com.teksiak.nutrilight.core.presentation.util.topBorder
import com.teksiak.nutrilight.product.presentation.product_details.components.NutritionFacts
import eu.wewox.textflow.material3.TextFlow
import eu.wewox.textflow.material3.TextFlowObstacleAlignment

@Composable
fun ProductDetailsScreenRoot(
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(ProductDetailsAction.LoadProduct(productId))
    }

    ProductDetailsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ProductDetailsAction.NavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ProductDetailsScreen(
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit
) {

    state.productUi?.let { productUi ->
        NutrilightScaffold(
            topAppBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomBorder(1.dp, ShadedWhite)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { onAction(ProductDetailsAction.NavigateBack) }
                    ) {
                        Icon(
                            imageVector = BackIcon,
                            contentDescription = "Back",
                            tint = TintedBlack
                        )
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = productUi.name,
                        style = MaterialTheme.typography.titleMedium,
                        softWrap = true,
                    )
                    CircleButton(
                        onClick = { },
                        icon = HeartIcon
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    ProductBasicInformation(
                        productUi = productUi,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .alpha(0.1f),
                        imageVector = LogoRottenIcon,
                        contentDescription = "No image",
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    )
                }
                NutrientContent(
                    nutrimentsUi = productUi.nutrimentsUi,
                    showNutritionFacts = state.showNutritionFacts,
                    onToggleNutritionFacts = { onAction(ProductDetailsAction.ToggleNutritionFacts) }
                )
                AnimatedVisibility(
                    visible = state.showNutritionFacts,
                    modifier = Modifier.fillMaxWidth(),
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(300)
                    ),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(300)
                    )
                ) {
                    NutritionFacts(
                        nutrimentsUi = productUi.nutrimentsUi
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextFlow(
                        text = buildAnnotatedString {
                            val style = MaterialTheme.typography.bodyLarge.toSpanStyle()
                            withStyle(
                                style = ParagraphStyle(
                                    lineHeight = 12.sp,
                                )
                            ) {
                                withStyle(
                                    style = style) {
                                    appendLine("${productUi.ingredientsAmount} Ingredients")
                                }
                            }

                            withStyle(
                                style = MaterialTheme.typography.bodyMedium
                                    .toSpanStyle()
                                    .copy(color = Silver)
                            ) {
                                append(productUi.ingredients)
                            }
                        },
                        obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
                    ) {
                        NutrilightScore(
                            score = productUi.score ?: 0f,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenPreview() {
    NutrilightTheme {
        ProductDetailsScreen(
            state = ProductDetailsState(
                productUi = DummyProduct.toProductUi().copy(
                    ingredients = "Cereal 50.7% (wheat flour 35%, _whole_wheat_ flour 15.7%), sugar, vegetable oils (palm, rapeseed), low-fat cocoa powder 4.5%, glucose syrup, wheat starch, raising agents (ammonium bicarbonate, baking soda, disodium diphosphate), emulsifier (soy lecithin, sunflower lecithin), salt, skimmed milk powder, lactose and milk protein, flavourings. May contain egg.",
                    ingredientsAmount = 20
                )
            ),
            onAction = { }
        )
    }
}