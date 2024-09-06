package com.teksiak.nutrilight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.home.HomeScreenRoot
import com.teksiak.nutrilight.home.HomeViewModel
import com.teksiak.nutrilight.scanner.presentation.ScannerScreenRoot
import com.teksiak.nutrilight.scanner.presentation.ScannerViewModel
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {

        composable<HomeScreen> {
            val homeViewModel = viewModel<HomeViewModel>()

            HomeScreenRoot(
                homeViewModel = homeViewModel,
                onScanBarcode = {
                    navController.navigate(ScannerScreen)
                }
            )
        }
        composable<ScannerScreen> {
            val viewModel = viewModel<ScannerViewModel>()

            ScannerScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                viewModel = viewModel
            )
        }
    }
}

@Serializable
object HomeScreen

@Serializable
object ScannerScreen