package com.teksiak.nutrilight.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.product.ProductUi

@Composable
fun ProductsList(
    productsList: List<ProductUi>,
    onFavouriteToggle: (String) -> Unit,
    onNavigateToProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
    emptyInformationText: AnnotatedString? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            productsList,
            key = { it.code }
        ) { productUi ->
            ProductCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                productUi = productUi,
                onFavouriteToggle = onFavouriteToggle,
                onNavigate = onNavigateToProduct
            )
        }
        if (productsList.isEmpty()) {
            emptyInformationText?.let { item ->
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        textAlign = TextAlign.Center,
                        text = item
                    )
                }
            }
        }
    }
}