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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.designsystem.HeartFilledIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightAppBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScore
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.product.presentation.product_details.components.NutrientContent
import com.teksiak.nutrilight.product.presentation.product_details.components.ProductBasicInformation
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.core.presentation.util.topBorder
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.product.presentation.product_details.components.NovaGroup
import com.teksiak.nutrilight.product.presentation.product_details.components.NutritionFacts
import eu.wewox.textflow.material3.TextFlow
import eu.wewox.textflow.material3.TextFlowObstacleAlignment
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsRoute(
    val productId: String
)

@Composable
fun ProductDetailsScreenRoot(
    viewModel: ProductDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                NutrilightAppBar(
                    title = productUi.name,
                    onNavigateBack = { onAction(ProductDetailsAction.NavigateBack) },
                    actionButtons = {
                        CircleButton(
                            onClick = { onAction(ProductDetailsAction.ToggleFavourite) },
                            iconTint = if (productUi.isFavourite) Color.Unspecified else TintedBlack,
                            icon = if (productUi.isFavourite) HeartFilledIcon else HeartIcon,
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp, bottom = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
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
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(
                                0f
                            )
                        })
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                NutrientContent(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    nutrimentsUi = productUi.nutrimentsUi,
                    showNutritionFacts = state.showNutritionFacts,
                    onToggleNutritionFacts = { onAction(ProductDetailsAction.ToggleNutritionFacts) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(
                    visible = state.showNutritionFacts,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(300)
                    ),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(300),
                    )
                ) {
                    NutritionFacts(
                        nutrimentsUi = productUi.nutrimentsUi,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                }
                productUi.novaGroup?.let {
                    NovaGroup(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        novaGroup = it
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextFlow(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    text = buildAnnotatedString {
                        val style = MaterialTheme.typography.bodyLarge.toSpanStyle()
                        withStyle(
                            style = ParagraphStyle(
                                lineHeight = 12.sp,
                            )
                        ) {
                            withStyle(
                                style = style
                            ) {
                                appendLine(
                                    if (productUi.ingredientsAmount != 0)
                                        "${productUi.ingredientsAmount} Ingredients"
                                    else "Ingredients"
                                )
                            }
                        }

                        withStyle(
                            style = MaterialTheme.typography.bodyMedium
                                .toSpanStyle()
                                .copy(color = Silver)
                        ) {
                            append(
                                if (productUi.ingredientsAmount != 0)
                                    productUi.ingredients
                                else "No available data"
                            )
                        }
                    },
                    obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
                ) {
                    NutrilightScore(
                        score = productUi.score,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                productUi.allergens
                    ?.takeIf { it.isNotBlank() }
                    ?.let { allergens ->
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = stringResource(id = R.string.allergens),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = allergens,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Silver
                        )
                    }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .topBorder(1.dp, ShadedWhite)
                        .padding(horizontal = 48.dp)
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        val style = MaterialTheme.typography.bodySmall
                        val dataResources = stringResource(id = R.string.data_resources) + " "
                        val showMore = stringResource(id = R.string.show_more)
                        withStyle(
                            style = ParagraphStyle(
                                lineHeight = 15.sp,
                            )
                        ) {
                            withStyle(
                                style = style.toSpanStyle()
                                    .copy(color = Silver)
                            ) {
                                append(dataResources)
                                withStyle(
                                    style = SpanStyle(
                                        color = Primary
                                    )
                                ) {
                                    append(showMore)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 900)
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