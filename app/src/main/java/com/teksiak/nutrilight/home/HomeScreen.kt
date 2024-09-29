package com.teksiak.nutrilight.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.designsystem.components.SearchBar
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel,
    onScanBarcode: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    HomeScreen(
        onAction = { action ->
            when(action) {
                HomeAction.ScanBarcode -> onScanBarcode()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun HomeScreen(
    onAction: (HomeAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    var searchValue by remember { mutableStateOf("") }

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
                    .padding(24.dp)
                    .height(40.dp)
            )
        },
        currentTab = NavigationTab.Home,
        onTabSelected = navigateWithTab
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = LogoIcon,
                contentDescription = "Logo",
                modifier = Modifier.size(128.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Nutrilight",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}