@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.teksiak.nutrilight.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightInformation
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.designsystem.components.RemoveFavouriteDialog
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchTopBar
import com.teksiak.nutrilight.core.presentation.ui_models.toProductUi
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.HomeScreenRoot(
    viewModel: HomeViewModel,
    onSearchProducts: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    navigateToTab: (NavigationTab) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.productsHistory.collectAsStateWithLifecycle()
    viewModel.favouriteProducts.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateBack()
    }

    HomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomeAction.SearchProducts -> onSearchProducts()
                HomeAction.ScanBarcode -> {
                    navigateToTab(NavigationTab.Scanner)
                }
                HomeAction.NavigateToMoreTab -> navigateToTab(NavigationTab.More)
                HomeAction.NavigateToProductsHistory -> navigateToTab(NavigationTab.History)
                HomeAction.NavigateToFavouriteProducts -> navigateToTab(NavigationTab.Favourites)
                is HomeAction.NavigateToProduct -> {
                    onNavigateToProduct(action.code)
                }

                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateToTab,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
private fun SharedTransitionScope.HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var heightOffsetLimit by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(heightOffsetLimit) {
        if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit
        }
    }

    state.productToRemove?.let {
        RemoveFavouriteDialog(
            onConfirm = { onAction(HomeAction.ConfirmRemoveFavourite) },
            onDismiss = { onAction(HomeAction.CancelRemoveFavourite) }
        )
    }

    NutrilightScaffold(
        topAppBar = {
            Layout(
                content = {
                    SearchTopBar(
                        searchValue = "",
                        onSearchValueChange = {},
                        onSearch = {},
                        onClear = {},
                        onScanBarClick = {
                            onAction(HomeAction.ScanBarcode)
                        },
                        onClick = { onAction(HomeAction.SearchProducts) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp)
                            .height(40.dp)
                            .sharedElement(
                                state = rememberSharedContentState(
                                    key = "searchBar",
                                ),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        isEnabled = false
                    )
                },
                measurePolicy = { measurables, constraints ->
                    val placeable = measurables.first().measure(constraints.copy(minWidth = 0))
                    heightOffsetLimit = placeable.height.toFloat() * -1
                    val scrollOffset = scrollBehavior.state.heightOffset
                    val height = placeable.height.toFloat() + scrollOffset
                    val layoutHeight = height.roundToInt()
                    layout(constraints.maxWidth, layoutHeight) {
                        placeable.place(0, scrollOffset.toInt())
                    }
                }
            )
        },
        currentTab = NavigationTab.Home,
        onTabSelected = navigateWithTab
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp),
        ) {
            item {
                NutrilightInformation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    title = stringResource(id = R.string.hey_you),
                    description = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.bodyMedium.toSpanStyle()
                        ) {
                            withStyle(
                                style = SpanStyle(color = TintedBlack)
                            ) {
                                append(stringResource(id = R.string.confused_about_product_information))
                                append(stringResource(id = R.string.see) + " ")
                            }
                            withLink(
                                link = LinkAnnotation.Clickable(
                                    tag = "more_tab",
                                    linkInteractionListener = {
                                        onAction(HomeAction.NavigateToMoreTab)
                                    }
                                )
                            ) {
                                withStyle(
                                    style = SpanStyle(color = Primary)
                                ) {
                                    append(stringResource(id = R.string.more_tab) + " ")
                                }

                            }
                            withStyle(
                                style = SpanStyle(color = TintedBlack)
                            ) {
                                append(stringResource(id = R.string.for_details))
                            }
                        }
                    }
                )
            }
            if (state.productsHistory.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.products_history),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .animateItem()
                    )
                }
            }

            items(
                items = state.productsHistory.take(3),
                key = { it.code + "-his" }
            ) { product ->
                ProductCard(
                    productUi = product.toProductUi(state.showProductImages),
                    onFavouriteToggle = { onAction(HomeAction.ToggleFavourite(it)) },
                    onNavigate = { onAction(HomeAction.NavigateToProduct(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateItem(),
                )
            }
            if (state.productsHistory.size > 3) {
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
                                        onAction(HomeAction.NavigateToProductsHistory)
                                    }
                                )
                            }
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .animateItem()
                    )
                }
            }

            if (state.favouriteProducts.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.favourite_products),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .animateItem()
                    )
                }
            }

            items(
                items = state.favouriteProducts.take(3),
                key = { it.code + "-fav" }
            ) { product ->
                ProductCard(
                    productUi = product.toProductUi(state.showProductImages),
                    onFavouriteToggle = { onAction(HomeAction.ToggleFavourite(it)) },
                    onNavigate = { onAction(HomeAction.NavigateToProduct(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateItem(),
                )
            }
            if (state.favouriteProducts.size > 3) {
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
                                        onAction(HomeAction.NavigateToFavouriteProducts)
                                    }
                                )
                            }
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .animateItem()
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}