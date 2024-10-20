package com.teksiak.nutrilight

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teksiak.nutrilight.core.presentation.NavigationRoute
import com.teksiak.nutrilight.core.presentation.toRoute
import com.teksiak.nutrilight.home.HomeScreenRoot
import com.teksiak.nutrilight.home.HomeViewModel
import com.teksiak.nutrilight.more.MoreScreenRoot
import com.teksiak.nutrilight.more.MoreViewModel
import com.teksiak.nutrilight.product.presentation.favourites.FavouritesScreenRoot
import com.teksiak.nutrilight.product.presentation.favourites.FavouritesViewModel
import com.teksiak.nutrilight.product.presentation.history.HistoryScreenRoot
import com.teksiak.nutrilight.product.presentation.history.HistoryViewModel
import com.teksiak.nutrilight.product.presentation.product_details.ProductDetailsScreenRoot
import com.teksiak.nutrilight.product.presentation.product_details.ProductDetailsViewModel
import com.teksiak.nutrilight.scanner.presentation.ScannerScreenRoot
import com.teksiak.nutrilight.scanner.presentation.ScannerViewModel
import com.teksiak.nutrilight.search.presentation.SearchScreenRoot
import com.teksiak.nutrilight.search.presentation.SearchViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    closeApp: () -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.HomeRoute,
        enterTransition = {
            fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400))
        }
    ) {

        composable<NavigationRoute.HomeRoute> {
            HomeScreenRoot(
                viewModel = viewModel<HomeViewModel>(viewModelStoreOwner),
                onSearchProducts = {
                    navController.navigate(NavigationRoute.SearchRoute)
                },
                onNavigateBack = {
                    closeApp()
                },
                onNavigateToProduct = { productId ->
                    navController.navigate(NavigationRoute.ProductDetailsRoute(productId))
                },
                navigateToTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavigationRoute.ScannerRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            ScannerScreenRoot(
                viewModel = hiltViewModel<ScannerViewModel>(),
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToProduct = { productId ->
                    navController.navigate(NavigationRoute.ProductDetailsRoute(productId)) {
                        popUpTo(NavigationRoute.ScannerRoute) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<NavigationRoute.SearchRoute> {
            SearchScreenRoot(
                viewModel = hiltViewModel<SearchViewModel>(),
                onScanBarcode = {
                    navController.navigate(NavigationRoute.ScannerRoute)
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToProduct = { productId ->
                    navController.navigate(NavigationRoute.ProductDetailsRoute(productId))
                }
            )
        }
        composable<NavigationRoute.ProductDetailsRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ProductDetailsScreenRoot(
                viewModel = hiltViewModel<ProductDetailsViewModel>(),
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.HistoryRoute> {
            HistoryScreenRoot(
                viewModel = hiltViewModel<HistoryViewModel>(viewModelStoreOwner),
                onNavigateToProduct = { code ->
                    navController.navigate(NavigationRoute.ProductDetailsRoute(code))
                },
                onNavigateBack = {
                    navController.navigate(NavigationRoute.HomeRoute) {
                        launchSingleTop = true
                    }
                },
                onSearchProduct = {
                    navController.navigate(NavigationRoute.SearchRoute)
                },
                onMoreInformation = { information ->
                    navController.navigate(NavigationRoute.MoreRoute(information)) {
                        launchSingleTop = true
                    }
                },
                navigateToTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavigationRoute.FavouritesRoute> {
            FavouritesScreenRoot(
                viewModel = hiltViewModel<FavouritesViewModel>(viewModelStoreOwner),
                onNavigateToProduct = { code ->
                    navController.navigate(NavigationRoute.ProductDetailsRoute(code))
                },
                onNavigateBack = {
                    navController.navigate(NavigationRoute.HomeRoute) {
                        launchSingleTop = true
                    }
                },
                onScanBarcode = {
                    navController.navigate(NavigationRoute.ScannerRoute)
                },
                navigateToTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavigationRoute.MoreRoute> {
            MoreScreenRoot(
                viewModel = hiltViewModel<MoreViewModel>(),
                onNavigateBack = {
                    navController.navigateUp()
                },
                navigateWithTab = { tab ->
                    navController.navigate(tab.toRoute()) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}