package com.teksiak.nutrilight.product.presentation.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teksiak.nutrilight.core.presentation.Information
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.CircleButton
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightAppBar
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.ui_models.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.core.presentation.designsystem.components.RemoveFavouriteDialog

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel,
    onNavigateToProduct: (String) -> Unit,
    onSearchProduct: () -> Unit,
    onNavigateBack: () -> Unit,
    onMoreInformation: (Information) -> Unit,
    navigateToTab: (NavigationTab) -> Unit
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
                is HistoryAction.ScanBarcode -> {
                    navigateToTab(NavigationTab.Scanner)
                }
                is HistoryAction.SearchProduct -> onSearchProduct()
                is HistoryAction.ShowHistorySizeSetting -> onMoreInformation(Information.HistorySize)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateToTab
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                state.productsHistory.map { it.toProductUi(state.showProductImages) },
                key = { it.code }
            ) { productUi ->
                ProductCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    productUi = productUi,
                    onFavouriteToggle = { onAction(HistoryAction.ToggleFavourite(it)) },
                    onNavigate = { onAction(HistoryAction.NavigateToProduct(it)) }
                )
            }
            if (state.productsHistory.isEmpty() && !state.isLoading) {
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
                                    append(stringResource(id = R.string.empty_history))
                                }
                                append("\n")
                                withStyle(
                                    style = SpanStyle(color = Primary)
                                ) {
                                    append(stringResource(id = R.string.try_scanning_something))
                                }
                            }
                        }
                    )
                }
            } else if (state.productsHistory.size == state.historySizeSetting && !state.isLoading) {
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
                                    append(stringResource(id = R.string.history_too_short))
                                }
                                append("\n")
                                withLink(
                                    link = LinkAnnotation.Clickable(
                                        tag = "more_tab",
                                        linkInteractionListener = {
                                            onAction(HistoryAction.ShowHistorySizeSetting)
                                        }
                                    )
                                ) {
                                    withStyle(
                                        style = SpanStyle(color = Primary)
                                    ) {
                                        append(stringResource(id = R.string.check_more_tab_to_adjust_size))
                                    }
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
        HistoryScreen(
            state = HistoryState()
                .copy(
                    productsHistory = listOf(
                        DummyProduct,
                        DummyProduct,
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