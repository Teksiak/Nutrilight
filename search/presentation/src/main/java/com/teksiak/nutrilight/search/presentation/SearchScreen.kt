package com.teksiak.nutrilight.search.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.presentation.BottomNavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.GlobeIcon
import com.teksiak.nutrilight.core.presentation.designsystem.LogoRottenIcon
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.components.LoadingAnimation
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.ProductCard
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
import com.teksiak.nutrilight.core.presentation.product.ProductUi
import com.teksiak.nutrilight.core.presentation.product.toProductUi

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel,
    onScanBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchedProducts = viewModel.searchedProducts.collectAsLazyPagingItems()

    BackHandler {
        onNavigateBack()
    }

    SearchScreen(
        state = state,
        searchedProducts = searchedProducts,
        onAction = { action ->
            viewModel.onAction(action)
            when (action) {
                is SearchAction.ScanBarcode -> onScanBarcode()
                is SearchAction.NavigateToProduct -> onNavigateToProduct(action.product.code)
                else -> Unit
            }
        }
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    searchedProducts: LazyPagingItems<Product>,
    onAction: (SearchAction) -> Unit
) {

    NutrilightScaffold(
        topAppBar = {
            SearchBar(
                searchValue = state.searchQuery,
                onSearchValueChange = { onAction(SearchAction.SearchQueryChanged(it)) },
                onSearch = { onAction(SearchAction.SearchProducts) },
                onClear = { onAction(SearchAction.ClearSearchQuery) },
                onScanBarClick = {
                    onAction(SearchAction.ScanBarcode)
                },
                focusOnComposition = !state.hasSearched,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
                    .height(40.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if(state.isLoading) return@Box
            if (searchedProducts.itemCount == 0 && !state.hasSearched) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        state.productsHistory,
                        key = { it.code }
                    ) { product ->
                        ProductCard(
                            modifier = Modifier
                                .fillMaxSize()
                                .animateItem(),
                            productUi = product.toProductUi(showImage = state.showProductImages),
                            onNavigate = { onAction(SearchAction.NavigateToProduct(product)) }
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier.animateItem(),
                            text = if(state.productsHistory.isEmpty()) {
                                stringResource(id = R.string.no_recent_matches)
                            } else if (state.searchQuery.isNotBlank()) {
                                stringResource(id = R.string.no_more_recent_matches)
                            } else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Silver,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (searchedProducts.loadState.refresh is LoadState.Loading) {
                Column(
                    modifier = Modifier.padding(top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingAnimation(
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.finding_your_match),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Silver
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = searchedProducts.itemCount,
                        key = searchedProducts.itemKey { it.code },
                    ) { index ->
                        searchedProducts[index]?.let { product ->
                            ProductCard(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateItem(),
                                productUi = product.toProductUi(showImage = state.showProductImages),
                                onNavigate = { onAction(SearchAction.NavigateToProduct(product)) }
                            )
                        }
                    }
                    if(searchedProducts.loadState.hasError) {
                        item {
                            Column(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .alpha(0.1f),
                                    imageVector = LogoRottenIcon,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                        setToSaturation(
                                            0f
                                        )
                                    })
                                )
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.whoops),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Silver
                                    )
                                    Text(
                                        text = stringResource(id = R.string.something_went_wrong_with_search),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Silver,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else if (searchedProducts.loadState.append is LoadState.Loading) {
                        item {
                            LoadingAnimation(
                                modifier = Modifier.size(48.dp).padding(top = 8.dp)
                            )
                        }
                    } else if(searchedProducts.loadState.append is LoadState.NotLoading && searchedProducts.itemCount > 0) {
                        item {
                            SearchWorldwide(
                                message = stringResource(id = R.string.havent_found_what_youre_looking_for),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    } else {
                        item {
                            SearchWorldwide(
                                message = stringResource(id = R.string.looks_like_nothing_came_up)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchWorldwide(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Silver
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.bodyMedium,
                color = Primary
            )
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp, end = 2.dp)
                    .size(18.dp),
                imageVector = GlobeIcon,
                contentDescription = null,
                tint = Primary,
            )
            Text(
                text = stringResource(id = R.string.worldwide),
                style = MaterialTheme.typography.bodyMedium,
                color = Primary
            )
        }
    }
}