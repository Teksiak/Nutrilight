package com.teksiak.nutrilight.product.presentation.favourites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.BackIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchInput
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.core.presentation.util.bottomBorder
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.product.presentation.components.ProductsList
import com.teksiak.nutrilight.product.presentation.components.RemoveFavouriteDialog
import kotlinx.serialization.Serializable

@Serializable
data object FavouritesRoute

@Composable
fun FavouritesScreenRoot(
    viewModel: FavouritesViewModel,
    onNavigateToProduct: (String) -> Unit,
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit,
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
        navigateWithTab = {
            if(state.isSearchActive) {
                viewModel.onAction(FavouritesAction.ToggleSearchbar)
                viewModel.onAction(FavouritesAction.ClearSearch)
            }
            navigateWithTab(it)
        }
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
        RemoveFavouriteDialog(
            onConfirm = { onAction(FavouritesAction.ConfirmRemoveFavourite) },
            onDismiss = { onAction(FavouritesAction.CancelRemoveFavourite) }
        )
    }

    var isSearchFocused by remember { mutableStateOf(false) }
    val isTyping = if (isSearchFocused) {
        state.searchQuery.isNotEmpty() && isSearchFocused
    } else state.searchQuery.isNotEmpty()

    NutrilightScaffold(
        topAppBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(1.dp, ShadedWhite)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        if (state.isSearchActive) {
                            onAction(FavouritesAction.ToggleSearchbar)
                            onAction(FavouritesAction.ClearSearch)
                        } else {
                            onAction(FavouritesAction.NavigateBack)
                        }
                    }
                ) {
                    Icon(
                        imageVector = BackIcon,
                        contentDescription = "Back",
                        tint = TintedBlack
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.weight(1f),
                        visible = !state.isSearchActive,
                        enter = expandHorizontally(
                            animationSpec = tween(300),
                            expandFrom = Alignment.End
                        ),
                        exit = shrinkHorizontally(
                            animationSpec = tween(300),
                            shrinkTowards = Alignment.End
                        )
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(id = R.string.favourites_title),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                    AnimatedContent(
                        targetState = state.isSearchActive,
                        transitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            ).togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(300)
                                )
                            )
                        }
                    ) { showSearch ->
                        if (showSearch) {
                            SearchInput(
                                modifier = Modifier,
                                value = state.searchQuery,
                                onValueChange = { onAction(FavouritesAction.SearchProducts(it)) },
                                onSearch = { },
                                onClear = { onAction(FavouritesAction.ClearSearch) },
                                onFocusChanged = { isFocused ->
                                    isSearchFocused = isFocused
                                },
                                focusOnComposition = true
                            )
                        } else {
                            CircleButton(
                                modifier = Modifier.wrapContentSize(),
                                icon = SearchIcon,
                                onClick = { onAction(FavouritesAction.ToggleSearchbar) }
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = !isTyping,
                    enter = expandHorizontally(
                        animationSpec = tween(300),
                        expandFrom = Alignment.Start,
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(300),
                        shrinkTowards = Alignment.Start,

                        )
                ) {
                    CircleButton(
                        icon = ScanBarIcon,
                        onClick = { onAction(FavouritesAction.ScanBarcode) },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        currentTab = NavigationTab.Favorites,
        onTabSelected = navigateWithTab
    ) { paddingValues ->
        ProductsList(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            productsList = state.favouriteProducts,
            onFavouriteToggle = { onAction(FavouritesAction.RemoveFavourite(it)) },
            onNavigateToProduct = { onAction(FavouritesAction.NavigateToProduct(it)) },
            emptyInformationText = if(!state.isLoading) buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.bodyMedium.toSpanStyle()
                ) {
                    withStyle(
                        style = SpanStyle(color = Silver)
                    ) {
                        if (state.searchQuery.isNotBlank()) {
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
            } else null
        )
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