package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.White

@Composable
fun NutrilightScaffold(
    modifier : Modifier = Modifier,
    topAppBar: @Composable () -> Unit = {},
    currentTab: NavigationTab? = null,
    onTabSelected: (NavigationTab) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
        topBar = topAppBar,
        bottomBar = {
            currentTab?.let { currentTab ->
                NutrilightNavBar(
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
            }
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}