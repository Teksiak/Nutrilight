package com.teksiak.nutrilight.product.presentation.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightAppBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.product.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.product.presentation.components.ProductsList
import com.teksiak.nutrilight.product.presentation.components.RemoveFavouriteDialog
import kotlinx.serialization.Serializable

@Serializable
data object HistoryRoute

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel,
    onNavigateToProduct: (String) -> Unit,
    onSearchProduct: () -> Unit,
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.productsHistory.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateBack()
    }

    HistoryScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is HistoryAction.NavigateToProduct -> onNavigateToProduct(action.code)
                is HistoryAction.NavigateBack -> onNavigateBack()
                is HistoryAction.ScanBarcode -> onScanBarcode()
                is HistoryAction.SearchProduct -> onSearchProduct()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    state.productToRemove?.let {
        RemoveFavouriteDialog(
            onConfirm = { onAction(HistoryAction.ConfirmRemoveFavourite) },
            onDismiss = { onAction(HistoryAction.CancelRemoveFavourite) }
        )
    }

    NutrilightScaffold(
        topAppBar = {
            NutrilightAppBar(
                title = stringResource(id = R.string.history),
                onNavigateBack = { onAction(HistoryAction.NavigateBack) },
                actionButtons = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircleButton(
                            icon = SearchIcon,
                            onClick = { onAction(HistoryAction.SearchProduct) }
                        )
                        CircleButton(
                            icon = ScanBarIcon,
                            onClick = { onAction(HistoryAction.ScanBarcode) }
                        )
                    }
                }
            )
        },
        currentTab = NavigationTab.History,
        onTabSelected = navigateWithTab
    ) { paddingValues ->
        ProductsList(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            productsList = state.productsHistory,
            onFavouriteToggle = { onAction(HistoryAction.ToggleFavourite(it)) },
            onNavigateToProduct = { onAction(HistoryAction.NavigateToProduct(it)) },
            emptyInformationText = if(!state.isLoading) buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.bodyMedium.toSpanStyle()
                ) {
                    withStyle(
                        style = SpanStyle(color = Silver)
                    ) {
                        append(stringResource(id = R.string.empty_history))
                    }
                    append("\n")
                    withStyle(
                        style = SpanStyle(color = Primary)
                    ) {
                        append(stringResource(id = R.string.try_scanning_something))
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
        HistoryScreen(
            state = HistoryState()
                .copy(
                    productsHistory = listOf(
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
        HistoryScreen(
            state = HistoryState(),
            onAction = {},
            navigateWithTab = {}
        )
    }
}