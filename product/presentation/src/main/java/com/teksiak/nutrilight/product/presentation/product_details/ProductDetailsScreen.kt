package com.teksiak.nutrilight.product.presentation.product_details

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.presentation.designsystem.BackIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.product.presentation.R

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
    state.product?.let { product ->
        NutrilightScaffold(
            topAppBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
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
                        text = product.name,
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
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BasicInformation(
                            label = stringResource(id = R.string.brands),
                            value = product.brands ?: "Unknown"
                        )
                        BasicInformation(
                            label = stringResource(id = R.string.quantity),
                            value = product.quantity ?: "Unknown"
                        )
                        BasicInformation(
                            label = stringResource(id = R.string.packaging),
                            value = product.packaging ?: "Unknown"
                        )
                    }
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
            }
        }
    }
}

@Composable
private fun BasicInformation(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Silver,
            softWrap = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenPreview() {
    NutrilightTheme {
        ProductDetailsScreen(
            state = ProductDetailsState(
                product = Product(
                    code = "20724696",
                    name = "Californian Almond test",
                    brands = "Alesto,Lidl,Solent",
                    quantity = "200g",
                    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
                    novaGroup = NovaGroup.NOVA_1,
                    nutriments = Nutriments(
                        energyKj = 2567f,
                        energyKcal = 621f,
                        fat = 53.3f,
                        saturatedFat = 4.3f,
                        carbohydrates = 4.8f,
                        sugars = 4.8f,
                        fiber = 12.1f,
                        protein = 24.5f,
                        salt = 0.01f
                    ),
                    allergens = listOf(
                        "Nuts"
                    ),
                    ingredients = listOf(
                        "almonds"
                    ),
                    score = 4.6f
                )
            ),
            onAction = { }
        )
    }
}