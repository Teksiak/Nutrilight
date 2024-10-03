package com.teksiak.nutrilight.home.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.product.ProductUi
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct

@Composable
fun ProductsListShortcut(
    title: String,
    productsList: List<ProductUi>,
    onNavigateToProduct: (String) -> Unit,
    onFavouriteToggle: (String) -> Unit,
    onSeeMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(368.dp),
            userScrollEnabled = false,
        ) {
            items(productsList.take(3)) { productUi ->
                ProductCard(
                    productUi = productUi,
                    onFavouriteToggle = { onFavouriteToggle(it) },
                    onNavigate = { onNavigateToProduct(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateItem(),
                )
            }
            if(productsList.size > 3) {
                item {
                    Text(
                        text = stringResource(R.string.see_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        onSeeMore()
                                    }
                                )
                            }
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .animateItem()
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsListShortcutPreview() {
    NutrilightTheme {
        ProductsListShortcut(
            productsList = listOf(
                DummyProduct.toProductUi(),
                DummyProduct.toProductUi(),
                DummyProduct.toProductUi(),
                DummyProduct.toProductUi(),
            ),
            title = "Products",
            onSeeMore = {},
            onNavigateToProduct = {},
            onFavouriteToggle = {},
        )
    }
}