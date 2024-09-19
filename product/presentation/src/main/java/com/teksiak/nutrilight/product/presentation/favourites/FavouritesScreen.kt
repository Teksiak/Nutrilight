package com.teksiak.nutrilight.product.presentation.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightAppBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.product.ProductUi
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.product.presentation.R
import kotlinx.serialization.Serializable

@Serializable
data object FavouritesRoute

@Composable
fun FavouritesScreenRoot(
    viewModel: FavouritesViewModel,
    onNavigateToProduct: (String) -> Unit,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    val favouriteProducts by viewModel.favouriteProducts.collectAsStateWithLifecycle(emptyList())

    FavouritesScreen(
        favouriteProducts = favouriteProducts,
        onAction = { action ->
            when (action) {
                is FavouritesAction.NavigateToProduct -> onNavigateToProduct(action.code)
                is FavouritesAction.NavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun FavouritesScreen(
    favouriteProducts: List<ProductUi>,
    onAction: (FavouritesAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    NutrilightScaffold(
        topAppBar = {
            NutrilightAppBar(
                title = stringResource(id = R.string.favourites_title),
                onNavigateBack = { onAction(FavouritesAction.NavigateBack) },
                actionButtons = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        CircleButton(
                            icon = SearchIcon,
                            onClick = { /*TODO*/ }
                        )
                        CircleButton(
                            icon = ScanBarIcon,
                            onClick = { /*TODO*/ }
                        )
                    }
                }
            )
        },
        currentTab = NavigationTab.Favorites,
        onTabSelected = navigateWithTab
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favouriteProducts) { productUi ->
                ProductCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    productUi = productUi,
                    onFavouriteToggle = { onAction(FavouritesAction.RemoveFavourite(it)) },
                    onNavigate = { onAction(FavouritesAction.NavigateToProduct(it)) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenPreview() {
    NutrilightTheme {
        FavouritesScreen(
            favouriteProducts = listOf(
                DummyProduct.toProductUi().copy(isFavourite = true),
                DummyProduct.toProductUi().copy(isFavourite = true),
            ),
            onAction = {},
            navigateWithTab = {}
        )
    }
}