@file:OptIn(ExperimentalMaterial3Api::class)

package com.teksiak.nutrilight.home

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.presentation.BottomNavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
import com.teksiak.nutrilight.product.presentation.components.RemoveFavouriteDialog
import kotlin.math.roundToInt

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel,
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    navigateWithTab: (BottomNavigationTab) -> Unit
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
                HomeAction.ScanBarcode -> onScanBarcode()
                HomeAction.NavigateToProductsHistory -> navigateWithTab(BottomNavigationTab.History)
                HomeAction.NavigateToFavouriteProducts -> navigateWithTab(BottomNavigationTab.Favourites)
                is HomeAction.NavigateToProduct -> {
                    onNavigateToProduct(action.code)
                }

                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigateWithTab: (BottomNavigationTab) -> Unit
) {
    var searchValue by remember { mutableStateOf("") }

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
                    SearchBar(
                        searchValue = searchValue,
                        onSearchValueChange = { searchValue = it },
                        onSearch = { },
                        onClear = { searchValue = "" },
                        onScanBarClick = {
                            onAction(HomeAction.ScanBarcode)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp)
                            .height(40.dp)
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
        currentTab = BottomNavigationTab.Home,
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
            if (state.productsHistory.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.products_history),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .animateItem()
                    )
                }
            }

            items(
                items = state.productsHistory.take(3),
                key = { it.code + "-his" }
            ) { productUi ->
                ProductCard(
                    productUi = productUi,
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
            ) { productUi ->
                ProductCard(
                    productUi = productUi,
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