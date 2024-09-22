package com.teksiak.nutrilight.product.presentation.favourites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.HeartXIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightAppBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.PrimaryButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchInput
import com.teksiak.nutrilight.core.presentation.designsystem.components.SecondaryButton
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
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FavouritesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is FavouritesAction.NavigateToProduct -> onNavigateToProduct(action.code)
                is FavouritesAction.NavigateBack -> onNavigateBack()
                is FavouritesAction.ScanBarcode -> onScanBarcode()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun FavouritesScreen(
    state: FavouritesState,
    onAction: (FavouritesAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    BackHandler(state.isSearchActive) {
        onAction(FavouritesAction.ToggleSearchbar)
        onAction(FavouritesAction.ClearSearch)
    }

    state.productToRemove?.let {
        NutrilightDialog(
            title = stringResource(id = R.string.oh_no),
            description = stringResource(id = R.string.remove_favourite_confirmation),
            onDismiss = { onAction(FavouritesAction.RemoveFavouriteCancellation) },
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = HeartXIcon,
                    contentDescription = null,
                    tint = Primary
                )
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SecondaryButton(
                        modifier = Modifier.width(128.dp),
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            onAction(FavouritesAction.RemoveFavouriteCancellation)
                        },
                        color = TintedBlack
                    )
                    PrimaryButton(
                        modifier = Modifier.width(128.dp),
                        text = stringResource(id = R.string.remove),
                        onClick = {
                            onAction(FavouritesAction.RemoveFavouriteConfirmation)
                        },
                    )
                }
            }
        )
    }

    NutrilightScaffold(
        topAppBar = {
            NutrilightAppBar(
                title = if(state.isSearchActive.not()) stringResource(id = R.string.favourites_title) else "",
                onNavigateBack = {
                    if(state.isSearchActive) {
                        onAction(FavouritesAction.ToggleSearchbar)
                        onAction(FavouritesAction.ClearSearch)
                    } else {
                        onAction(FavouritesAction.NavigateBack)
                    }
                },
                actionButtons = {
                    if (state.isSearchActive) {
                        SearchBar(
                            searchValue = state.searchQuery,
                            onSearchValueChange = { onAction(FavouritesAction.SearchProducts(it)) },
                            onSearch = { },
                            onClear = { onAction(FavouritesAction.ClearSearch) },
                            onScanBarClick = { onAction(FavouritesAction.ScanBarcode) },
                            focusOnComposition = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircleButton(
                                icon = SearchIcon,
                                onClick = { onAction(FavouritesAction.ToggleSearchbar) }
                            )
                            CircleButton(
                                icon = ScanBarIcon,
                                onClick = { onAction(FavouritesAction.ScanBarcode) }
                            )
                        }
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
            items(
                state.favouriteProducts,
                key = { it.code }
            ) { productUi ->
                ProductCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    productUi = productUi,
                    onFavouriteToggle = { onAction(FavouritesAction.RemoveFavourite(it)) },
                    onNavigate = { onAction(FavouritesAction.NavigateToProduct(it)) }
                )
            }
            if(state.favouriteProducts.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.bodyMedium.toSpanStyle()
                            ) {
                                withStyle(
                                    style = SpanStyle(color = Silver)
                                ) {
                                    if(state.searchQuery.isNotBlank()) {
                                        append(stringResource(id = R.string.no_favourite_matches))
                                    } else {
                                        append(stringResource(id = R.string.no_favourites_added))
                                    }
                                }
                                append("\n")
                                withStyle(
                                    style = SpanStyle(color = Primary)
                                ) {
                                    append(stringResource(id = R.string.save_your_top_picks))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenPreview() {
    NutrilightTheme {
        FavouritesScreen(
            state = FavouritesState()
                .copy(
                    favouriteProducts = listOf(
                        DummyProduct.toProductUi().copy(isFavourite = true),
                        DummyProduct.toProductUi().copy(isFavourite = true),
                    )
                ),
            onAction = {},
            navigateWithTab = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesEmptyScreenPreview() {
    NutrilightTheme {
        FavouritesScreen(
            state = FavouritesState(),
            onAction = {},
            navigateWithTab = {}
        )
    }
}