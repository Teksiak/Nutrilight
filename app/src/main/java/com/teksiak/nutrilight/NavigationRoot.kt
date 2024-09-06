package com.teksiak.nutrilight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
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