package com.teksiak.nutrilight

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {

        composable<HomeRoute> {
            val homeViewModel = viewModel<HomeViewModel>()

            HomeScreenRoot(
                homeViewModel = homeViewModel,
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
            val viewModel = hiltViewModel<ScannerViewModel>()

            ScannerScreenRoot(
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
                viewModel = viewModel
            )
        }
        composable<ProductDetailsRoute> {
            val viewModel = hiltViewModel<ProductDetailsViewModel>()

            ProductDetailsScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                viewModel = viewModel
            )
        }
        composable<HistoryRoute> {
            val viewModel = hiltViewModel<HistoryViewModel>()

            HistoryScreenRoot(
                viewModel = viewModel,
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
            val viewModel = hiltViewModel<FavouritesViewModel>()

            FavouritesScreenRoot(
                viewModel = viewModel,
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