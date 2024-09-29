package com.teksiak.nutrilight

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.home.HomeRoute
import com.teksiak.nutrilight.home.HomeScreenRoot
import com.teksiak.nutrilight.home.HomeViewModel
import com.teksiak.nutrilight.product.presentation.favourites.FavouritesRoute
import com.teksiak.nutrilight.product.presentation.favourites.FavouritesScreenRoot
import com.teksiak.nutrilight.product.presentation.favourites.FavouritesViewModel
import com.teksiak.nutrilight.product.presentation.history.HistoryRoute
import com.teksiak.nutrilight.product.presentation.history.HistoryScreenRoot
import com.teksiak.nutrilight.product.presentation.history.HistoryViewModel
import com.teksiak.nutrilight.product.presentation.product_details.ProductDetailsRoute
import com.teksiak.nutrilight.product.presentation.product_details.ProductDetailsScreenRoot
import com.teksiak.nutrilight.product.presentation.product_details.ProductDetailsViewModel
import com.teksiak.nutrilight.scanner.presentation.ScannerRoute
import com.teksiak.nutrilight.scanner.presentation.ScannerScreenRoot
import com.teksiak.nutrilight.scanner.presentation.ScannerViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None }
    ) {

        composable<HomeRoute> {

            HomeScreenRoot(
                viewModel = viewModel<HomeViewModel>(viewModelStoreOwner),
                onScanBarcode = {
                    navController.navigate(ScannerRoute)
                },
                navigateWithTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        popUpTo(HomeRoute) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<ScannerRoute> {
            ScannerScreenRoot(
                viewModel = hiltViewModel<ScannerViewModel>(),
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToProduct = { productId ->
                    navController.navigate(ProductDetailsRoute(productId)) {
                        popUpTo(ScannerRoute) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<ProductDetailsRoute> {
            ProductDetailsScreenRoot(
                viewModel = hiltViewModel<ProductDetailsViewModel>(),
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<HistoryRoute> {
            HistoryScreenRoot(
                viewModel = hiltViewModel<HistoryViewModel>(viewModelStoreOwner),
                onNavigateToProduct = { code ->
                    navController.navigate(ProductDetailsRoute(code))
                },
                onNavigateBack = {
                    navController.navigate(HomeRoute) {
                        popUpTo(HistoryRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSearchProduct = { },
                onScanBarcode = {
                    navController.navigate(ScannerRoute)
                },
                navigateWithTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        popUpTo(HomeRoute) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<FavouritesRoute> {
            FavouritesScreenRoot(
                viewModel = hiltViewModel<FavouritesViewModel>(viewModelStoreOwner),
                onNavigateToProduct = { code ->
                    navController.navigate(ProductDetailsRoute(code))
                },
                onNavigateBack = {
                    navController.navigate(HomeRoute) {
                        popUpTo(FavouritesRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onScanBarcode = {
                    navController.navigate(ScannerRoute)
                },
                navigateWithTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        popUpTo(HomeRoute) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun NavigationTab.toRoute(): Any {
    return when (this) {
        NavigationTab.Home -> HomeRoute
        NavigationTab.History -> HistoryRoute
        NavigationTab.Scanner -> ScannerRoute
        NavigationTab.Favorites -> FavouritesRoute
        NavigationTab.More -> HomeRoute
    }
}