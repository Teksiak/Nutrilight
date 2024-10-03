package com.teksiak.nutrilight.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
import com.teksiak.nutrilight.home.components.ProductsListShortcut
import com.teksiak.nutrilight.product.presentation.components.RemoveFavouriteDialog
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel,
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProduct : (String) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
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
            when(action) {
                HomeAction.ScanBarcode -> onScanBarcode()
                HomeAction.NavigateToProductsHistory -> navigateWithTab(NavigationTab.History)
                HomeAction.NavigateToFavouriteProducts -> navigateWithTab(NavigationTab.Favourites)
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
    navigateWithTab: (NavigationTab) -> Unit
) {
    var searchValue by remember { mutableStateOf("") }

    state.productToRemove?.let {
        RemoveFavouriteDialog(
            onConfirm = { onAction(HomeAction.ConfirmRemoveFavourite) },
            onDismiss = { onAction(HomeAction.CancelRemoveFavourite) }
        )
    }

    NutrilightScaffold(
        topAppBar = {
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
        currentTab = NavigationTab.Home,
        onTabSelected = navigateWithTab
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if(state.productsHistory.isNotEmpty()) {
                ProductsListShortcut(
                    title = stringResource(id = R.string.products_history),
                    productsList = state.productsHistory,
                    onSeeMore = { onAction(HomeAction.NavigateToProductsHistory) },
                    onNavigateToProduct = { onAction(HomeAction.NavigateToProduct(it)) },
                    onFavouriteToggle = { onAction(HomeAction.ToggleFavourite(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if(state.favouriteProducts.isNotEmpty()) {
                ProductsListShortcut(
                    title = stringResource(id = R.string.favourite_products),
                    productsList = state.favouriteProducts,
                    onSeeMore = { onAction(HomeAction.NavigateToFavouriteProducts) },
                    onNavigateToProduct = { onAction(HomeAction.NavigateToProduct(it)) },
                    onFavouriteToggle = { onAction(HomeAction.ToggleFavourite(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}